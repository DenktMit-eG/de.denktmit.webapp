package de.denktmit.webapp.business.security


import de.denktmit.webapp.business.user.UserRepository
import de.denktmit.webapp.springconfig.BusinessContextConfigProperties
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*
import org.springframework.security.core.userdetails.User as SpringUser

@Service
class WebappUserManagementService(
    private val config: BusinessContextConfigProperties,
    private val userRepository: UserRepository,
) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(name: String): UserDetails {
        return userRepository.findByMail(name)?.let { user ->
            val now = Instant.now()
            val lockedUntil = user.lockedUntil
            val roles = if (config.adminUsers.contains(user.mail)) {
                "ROLE_ADMIN"
            } else {
                "ROLE_${user.role.name}"
            }
            SpringUser.builder()
                .accountExpired(now.isAfter(user.accountValidUntil))
                .accountLocked(lockedUntil != null && now.isBefore(lockedUntil))
                .authorities(roles)
                .credentialsExpired(now.isAfter(user.credentialsValidUntil))
                .disabled(user.disabled)
                .password(user.password.trimEnd())
                .username(user.mail)
                .build()
        }?: throw UsernameNotFoundException("Username or password is wrong for $name")
    }

}