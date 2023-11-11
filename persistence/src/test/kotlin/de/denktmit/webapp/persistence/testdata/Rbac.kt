package de.denktmit.webapp.persistence.testdata

import de.denktmit.webapp.persistence.rbac.Authority
import de.denktmit.webapp.persistence.rbac.Group
import de.denktmit.webapp.persistence.rbac.RbacMapping
import de.denktmit.webapp.persistence.testdata.Users.janesmith

object Rbac {

    val groupAdmins = Group(10001L, "administrators")
    val groupUsers = Group(10001L, "users")

    val roleAdmin = Authority(10001L, "ADMIN")
    val roleUser = Authority(10002L, "USER")

    val janeSmithRbac = RbacMapping(
        janesmith,
        setOf(groupAdmins),
        setOf(roleAdmin, roleUser)
    )

}