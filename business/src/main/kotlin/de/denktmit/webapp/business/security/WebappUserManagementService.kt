package de.denktmit.webapp.business.security

import de.denktmit.webapp.persistence.otp.OtpRepository
import de.denktmit.webapp.persistence.rbac.RbacRepository
import de.denktmit.webapp.persistence.users.User
import de.denktmit.webapp.persistence.users.UserRepository
import de.denktmit.webapp.springconfig.BusinessContextConfigProperties
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.Principal
import java.time.Instant
import java.util.*
import org.springframework.security.core.userdetails.User as SpringUser

@Service
@Transactional(readOnly = true)
class WebappUserManagementService(
    private val config: BusinessContextConfigProperties,
    private val passwordEncoder: PasswordEncoder,
    private val rbacRepository: RbacRepository,
    private val otpRepository: OtpRepository,
    private val userRepository: UserRepository,
) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(name: String): UserDetails {
        return rbacRepository.findByMail(name)?.let { (user, _, auths) ->
            val now = Instant.now()
            val roles = if (config.adminUsers.contains(user.mail)) {
                arrayOf("ROLE_ADMIN", "ROLE_USER")

            } else {
                auths.map { authority -> "ROLE_${authority.authority}" }.toTypedArray()
            }
            SpringUser.builder()
                .accountExpired(now.isAfter(user.accountValidUntil))
                .accountLocked(now.isBefore(user.lockedUntil))
                .authorities(*roles)
                .credentialsExpired(now.isAfter(user.credentialsValidUntil))
                .disabled(user.disabled)
                .password(user.password.trimEnd())
                .username(user.mail)
                .build()
        }?: throw UsernameNotFoundException("Username or password is wrong for $name")
    }

    @Transactional
    fun updatePassword(principal: Principal, oldPassword: WipeableCharSequence, newPassword: WipeableCharSequence): Result<User> {
        val user = userRepository.findOneByMail(principal.name)

        if (user == null) {
            oldPassword.wipe()
            newPassword.wipe()
            return Result.failure(UsernameNotFoundException("No user found for username '${principal.name}'"))
        }

        if (!passwordEncoder.matches(oldPassword, user.password)) {
            oldPassword.wipe()
            newPassword.wipe()
            return Result.failure(BadCredentialsException("Old password does not match for username '${principal.name}'"))
        }

        val newEncodedPassword = passwordEncoder.encode(newPassword)
        val updatedUser = user.copy(password = newEncodedPassword)
        oldPassword.wipe()
        newPassword.wipe()

        return try {
            Result.success(userRepository.save(updatedUser))
        } catch (e: Throwable) {
            Result.failure(e)
        }

    }

//    @Transactional
//    fun resetPassword(token: String, newPassword: String): Result<User> {
//        val user = userRepository.findByMail(principal.name)
//    }

}