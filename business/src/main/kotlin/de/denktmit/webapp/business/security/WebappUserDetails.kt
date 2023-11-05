package de.denktmit.webapp.business.security

import de.denktmit.webapp.persistence.users.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.User as SpringUser


class WebappUserDetails private constructor(
    username: String,
    password: String,
    enabled: Boolean = true,
    accountNonExpired: Boolean = true,
    credentialsNonExpired: Boolean = true,
    accountNonLocked: Boolean = true,
    authorities: Collection<GrantedAuthority> = AuthorityUtils.createAuthorityList("ROLE_GUEST")
): SpringUser(
    username,
    password,
    enabled,
    accountNonExpired,
    credentialsNonExpired,
    accountNonLocked,
    authorities,
) {

    constructor(user: User, vararg authorities: String = arrayOf("ROLE_USER")) : this(
        username = user.mail,
        password = user.password,
        enabled = user.enabled,
        authorities = AuthorityUtils.createAuthorityList(*authorities)
    )

}