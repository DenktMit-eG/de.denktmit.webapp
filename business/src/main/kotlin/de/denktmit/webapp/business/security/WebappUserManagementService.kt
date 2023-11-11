package de.denktmit.webapp.business.security


import de.denktmit.webapp.persistence.rbac.RbacRepository
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
    private val rbacRepository: RbacRepository,
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

}