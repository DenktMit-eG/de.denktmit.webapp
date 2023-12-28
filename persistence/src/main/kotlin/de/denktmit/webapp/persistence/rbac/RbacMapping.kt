package de.denktmit.webapp.persistence.rbac

import de.denktmit.webapp.persistence.users.UserEntity

data class RbacMapping(
    val user: UserEntity,
    val groups: Set<Group>,
    val authorities: Set<Authority>,
)