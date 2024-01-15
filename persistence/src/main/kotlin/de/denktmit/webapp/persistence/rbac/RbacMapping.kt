package de.denktmit.webapp.persistence.rbac

import de.denktmit.webapp.persistence.users.User

data class RbacMapping(
    val user: User,
    val groups: Set<Group>,
    val authorities: Set<Authority>,
)