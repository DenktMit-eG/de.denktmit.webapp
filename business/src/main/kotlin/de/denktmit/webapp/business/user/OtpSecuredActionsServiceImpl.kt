package de.denktmit.webapp.business.user

import de.denktmit.webapp.business.communication.PasswordRecoveryRequestedEvent
import de.denktmit.webapp.business.user.UserService.UserSavingResult
import de.denktmit.webapp.persistence.Constants.FAR_FUTURE
import de.denktmit.webapp.persistence.otp.OtpAction
import de.denktmit.webapp.persistence.otp.OtpRepository
import de.denktmit.webapp.persistence.users.User
import de.denktmit.webapp.springconfig.BusinessContextConfigProperties
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.net.URI
import java.time.Duration
import java.time.Instant
import java.util.*

@Service
@Transactional(readOnly = true)
class OtpSecuredActionsServiceImpl(
    private val config: BusinessContextConfigProperties,
    private val publisher: ApplicationEventPublisher,
    private val userService: UserService,
    private val otpRepository: OtpRepository
) : OtpSecuredActionsService {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(OtpSecuredActionsServiceImpl::class.java)
        const val ACTION_ACCEPT_INVITE = "accept-invite"
        const val ACTION_RESET_PASSWORD = "reset-password"
        const val ACTION_VERIFY_EMAIL = "verify-email"
    }

    override fun isTokenValid(token: UUID, action: String, instant: Instant): Boolean {
        return otpRepository.existsByTokenAndActionAndValidUntilAfter(token, action, instant)
    }

    @Transactional
    override fun createOtpAction(
        user: User,
        action: String,
        duration: Duration
    ): OtpAction {
        otpRepository.deleteByUserMailAndAction(user.mail, action)
        return otpRepository.save(OtpAction.createOtpAction(user, action, duration))
    }

    @Transactional
    override fun validateEmailAddress(
        token: UUID,
        instant: Instant
    ): OtpActionResult<UserSavingResult, TokenNotResolved> {
        return resolveThenDo(token, ACTION_VERIFY_EMAIL, instant) { otpAction ->
            userService.updateUser(otpAction.user.copy(mailVerified = true)).also { userSavingResult ->
                when (userSavingResult) {
                    is UserSavingResult.Persisted -> LOGGER.info("Successfully validated email for user ${otpAction.user}")
                    else -> LOGGER.error("Could not validate email for user ${otpAction.user}, saving result was $userSavingResult")
                }
            }
        }
    }

    @Transactional
    override fun acceptInvitation(
        token: UUID,
        newPassword: WipeableCharSequence,
        instant: Instant
    ): OtpActionResult<UserSavingResult, TokenNotResolved> {
        return resolveThenDo(token, ACTION_ACCEPT_INVITE, instant) { otpAction ->
            val updatedUser = otpAction.user.copy(mailVerified = true)
            saveWithNewPassword(updatedUser, newPassword).also { userSavingResult ->
                when (userSavingResult) {
                    is UserSavingResult.Persisted -> LOGGER.info("Successfully accepted invitation by $updatedUser")
                    else -> LOGGER.error("Could not process invitation accept by $updatedUser")
                }
            }
        }
    }

    @Transactional
    override fun startPasswordRecovery(mail: String, passwordResetUri: URI) {
        val user = userService.findOneByMail(mail)
        if (user == null) {
            LOGGER.warn("Password recovery process failed for unknown email '$mail'")
            return
        }
        val otpAction = createOtpAction(user, ACTION_RESET_PASSWORD, config.resetPasswordDuration)
        publisher.publishEvent(
            PasswordRecoveryRequestedEvent(
                user,
                otpAction.createOtpCallbackUri(passwordResetUri),
                otpAction.validUntil,
                LocaleContextHolder.getLocale()
            )
        )
    }

    @Transactional
    override fun resetPassword(
        token: UUID,
        newPassword: WipeableCharSequence,
        instant: Instant
    ): OtpActionResult<UserSavingResult, TokenNotResolved> {
        return resolveThenDo(token, ACTION_RESET_PASSWORD, instant) { otpAction ->
            saveWithNewPassword(otpAction.user, newPassword).also { userSavingResult ->
                when (userSavingResult) {
                    is UserSavingResult.Persisted -> LOGGER.info("Successfully reset password for ${otpAction.user}")
                    else -> LOGGER.error("Could not process password reset for ${otpAction.user}")
                }
            }
        }
    }

    private fun <T : Any> resolveThenDo(
        token: UUID,
        action: String,
        validationTime: Instant,
        fn: (action: OtpAction) -> T
    ): OtpActionResult<T, TokenNotResolved> {
        return when (val result = resolveToken(token, action, validationTime)) {
            is OtpActionResult.Failed -> OtpActionResult.Failed(result.errorPayload)
            is OtpActionResult.Success -> OtpActionResult.Success(fn(result.payload))
        }
    }

    private fun resolveToken(
        token: UUID,
        action: String,
        validationTime: Instant
    ): OtpActionResult<OtpAction, TokenNotResolved> {
        val otpAction = otpRepository.findOneByTokenAndActionAndValidUntilAfter(token, action, validationTime)
        if (otpAction == null) {
            LOGGER.warn("No token found for token '$token', action '$action' and instant '$validationTime'")
            return OtpActionResult.Failed(TokenNotResolved(token, action, validationTime))
        }
        otpRepository.delete(otpAction)
        return OtpActionResult.Success(otpAction)
    }

    private fun saveWithNewPassword(user: User, newPassword: WipeableCharSequence): UserSavingResult =
        userService.updateUser(
            user.copy(
                password = userService.encodePassword(newPassword),
                credentialsValidUntil = FAR_FUTURE
            )
        )

}