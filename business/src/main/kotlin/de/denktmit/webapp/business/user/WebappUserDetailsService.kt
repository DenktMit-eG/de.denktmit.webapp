package de.denktmit.webapp.business.user

import de.denktmit.webapp.persistence.rbac.RbacRepository
import de.denktmit.webapp.springconfig.BusinessContextConfigProperties
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*
import org.springframework.security.core.userdetails.User as SpringUser

@Service
@Transactional(readOnly = true)
class WebappUserDetailsService(
    private val config: BusinessContextConfigProperties,
    private val rbacRepository: RbacRepository,
) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(name: String): UserDetails {
        return rbacRepository.findOneByMail(name)?.let { (user, _, auths) ->
            val now = Instant.now()
            val roles = if (config.adminUsers.contains(user.mail)) {
                arrayOf("ROLE_ADMIN")
            } else if (!user.mailVerified) {
                arrayOf("ROLE_USER_UNVERIFIED")
            } else {
                auths.map { authority -> "ROLE_${authority.authority}" }.toTypedArray()
            }
            val userDetailsBuilder = SpringUser.builder()
                .accountExpired(now.isAfter(user.accountValidUntil))
                .accountLocked(now.isBefore(user.lockedUntil))
                .authorities(*roles)
                .credentialsExpired(now.isAfter(user.credentialsValidUntil))
                .disabled(user.disabled)
                .password(user.password.trimEnd())
                .username(user.mail)
            userDetailsBuilder.build()
        }?: throw UsernameNotFoundException("Username or password is wrong for $name")
    }

}