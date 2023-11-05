package de.denktmit.webapp.business.security


import de.denktmit.webapp.persistence.users.UserRepository
import de.denktmit.webapp.springconfig.BusinessContextConfigProperties
import org.springframework.security.core.userdetails.User
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
            val username = user.mail
            val hashedPassword = if (user.password.matches("^\\{[a-z0-9]+}.+".toRegex())) user.password else "{noop}" + user.password
            User.builder()
                .accountExpired(false)
                .accountLocked(false)
                .disabled(user.disabled)
                .credentialsExpired(false)
                .authorities(listOf())
                .username(username)
                .password(hashedPassword)
                .build()


            val myUserDetails = if (config.adminUsers.contains(user.mail)) {
                WebappUserDetails(user, "ROLE_ADMIN")
            } else {
                WebappUserDetails(user, "ROLE_${user.role.name}")
            }
            myUserDetails
        }?: throw UsernameNotFoundException("Username or password is wrong for $name")
    }

}