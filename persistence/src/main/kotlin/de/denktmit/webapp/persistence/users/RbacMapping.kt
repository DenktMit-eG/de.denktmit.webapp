package de.denktmit.webapp.persistence.users

data class RbacMapping(
    val user: User,
    val groups: Set<Group>,
    val authorities: Set<Authority>,
)