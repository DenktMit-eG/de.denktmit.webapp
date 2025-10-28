package de.denktmit.webapp.business.user

import de.denktmit.webapp.business.user.UserService.UserSavingResult
import de.denktmit.webapp.common.DataTable
import de.denktmit.webapp.persistence.Constants
import de.denktmit.webapp.persistence.users.GROUP_NAME_USERS
import de.denktmit.webapp.persistence.users.RbacMapping
import de.denktmit.webapp.persistence.users.RbacRepository
import de.denktmit.webapp.persistence.users.User
import de.denktmit.webapp.persistence.users.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.interceptor.TransactionAspectSupport
import java.net.URI
import java.security.Principal
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Service
@Transactional(readOnly = true)
class UserServiceImpl(
    private val publisher: ApplicationEventPublisher,
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository,
    private val rbacRepository: RbacRepository,
): UserService {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(UserServiceImpl::class.java)
        private val FORMATTER: DateTimeFormatter = DateTimeFormatter.RFC_1123_DATE_TIME
    }

    override fun userDataV2(): List<RbacMapping> {

        val mails = userRepository
            .findAll()
            .map { it.mail }
            .toSet()
        return rbacRepository.findAllByMails(mails)
    }

    override fun userData(): DataTable {
        val meta = DataTable.Meta.create("Groups", "E-Mail", "Disabled", "Locked Until", "Account Valid Until", "Credentials Valid Until")
        val mails = userRepository
            .findAll()
            .map { it.mail }
            .toSet()
        val rbacMappings = rbacRepository.findAllByMails(mails)
        val rows = rbacMappings.map { entry ->
            meta.createRow(
                entry.groups.joinToString(",") { g -> g.groupName },
                entry.user.mail,
                entry.user.disabled,
                entry.user.lockedUntil.toDateTimeString(),
                entry.user.accountValidUntil.toDateTimeString(),
                entry.user.credentialsValidUntil.toDateTimeString(),
            )
        }
        return DataTable.create(meta, rows)
    }

    fun Instant.toDateTimeString(zoneId: ZoneId = ZoneOffset.UTC, formatter: DateTimeFormatter = FORMATTER): String {
        val zonedDateTime = ZonedDateTime.ofInstant(this, zoneId)
        return zonedDateTime.format(formatter)
    }

    override fun isEmailExisting(mail: String): Boolean {
        return userRepository.existsByMail(mail)
    }

    override fun findOneByMail(mail: String): User? {
        return userRepository.findOneByMail(mail)
    }

    override fun encodePassword(password: WipeableCharSequence): String {
        val encodedPassword = passwordEncoder.encode(password)
        password.wipe()
        return encodedPassword
    }

    @Transactional
    override fun createUser(
        mail: String,
        password: WipeableCharSequence,
        mailVerificationUri: URI,
        mailVerified: Boolean
    ): UserSavingResult {
        val unsavedUser = User.create(mail, passwordEncoder.encode(password), mailVerified = mailVerified)
        val result = persistUser(unsavedUser, setOf(GROUP_NAME_USERS))
        if (result is UserSavingResult.Persisted) {
            publisher.publishEvent(UserCreatedEvent(result.user, mailVerificationUri, LocaleContextHolder.getLocale()))
        }
        return result
    }

    @Transactional
    override fun updateUser(unsavedUser: User): UserSavingResult {
        return persistUser(unsavedUser)
    }

    @Transactional
    override fun inviteUser(
        mail: String,
        groupName: String,
        invitationAcceptUri: URI,
        mailVerified: Boolean
    ): UserSavingResult {
        val unsavedUser = User.create(
            mail,
            encodePassword(generateRandomPassword()),
            mailVerified = true,
            credentialsValidUntil = Constants.FAR_PAST
        )
        val result = persistUser(unsavedUser, setOf(groupName))
        if (result is UserSavingResult.Persisted) {
            publisher.publishEvent(UserInvitedEvent(result.user, invitationAcceptUri, LocaleContextHolder.getLocale()))
        }
        return result
    }

    private fun generateRandomPassword(length: Int = 20): WipeableCharSequence {
        val allowedChars = ('a'..'z') + ('A'..'Z') + ('0'..'9') + "!@#$%^&*()-=_+[]{}|;:'\",.<>/?".toList()
        val charArray = (1..length)
            .map { allowedChars.random() }
            .toCharArray()
        return WipeableCharSequence(charArray)
    }



    @Transactional
    override fun updatePassword(principal: Principal, oldPassword: WipeableCharSequence, newPassword: WipeableCharSequence): UserSavingResult {
        try {
            val user = userRepository.findOneByMail(principal.name)
                ?: return UserSavingResult.UserNotFound(principal.name)
            if (!passwordEncoder.matches(oldPassword, user.password)) {
                return UserSavingResult.BadCredentialsProvided(user)
            }
            val newEncodedPassword = passwordEncoder.encode(newPassword)
            val unsavedUser = user.copy(password = newEncodedPassword)
            return updateUser(unsavedUser)
        } finally {
            oldPassword.wipe()
            newPassword.wipe()
        }
    }

    private fun persistUser(
        unsavedUser: User,
        userGroups: Set<String>? = null,
    ): UserSavingResult {
        return try {
            val user = userRepository.save(unsavedUser)
            if (userGroups != null) {
                rbacRepository.setUserGroups(unsavedUser.mail, userGroups)
                LOGGER.info("Created invited $user and mapped RBAC groups '${userGroups.joinToString()}'")
            }
            UserSavingResult.Persisted(user)
        } catch (throwable: Throwable) {
            handleUserSavingError(unsavedUser, throwable)
        }
    }

    private fun handleUserSavingError(unsavedUser: User, throwable: Throwable): UserSavingResult {
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()
        if (throwable is DataIntegrityViolationException && throwable.message?.contains("uq_users_mail") == true) {
            LOGGER.error("Not created/updated $unsavedUser, email address already exists")
            return UserSavingResult.EmailAlreadyExists(unsavedUser)
        }
        LOGGER.error("Not created/updated $unsavedUser, an unexpected error occurred", throwable)
        throw throwable
    }
}