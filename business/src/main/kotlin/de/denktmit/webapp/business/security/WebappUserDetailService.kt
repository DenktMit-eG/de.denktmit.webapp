package de.denktmit.webapp.business.security


import de.denktmit.webapp.persistence.users.UserRepository
import de.denktmit.webapp.springconfig.BusinessContextConfigProperties
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.*

@Service
class WebappUserDetailService(
    private val config: BusinessContextConfigProperties,
    private val userRepository: UserRepository
) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(name: String): UserDetails {
        return userRepository.findByMail(name)?.let { user ->

            val myUserDetails = if (config.adminUsers.contains(user.mail)) {
                WebappUserDetails(user, "ROLE_ADMIN")
            } else {
                WebappUserDetails(user, "ROLE_${user.role.name}")
            }
            myUserDetails
        }?: throw UsernameNotFoundException("Username or password is wrong for $name")
    }

}