package de.denktmit.webapp.persistence.testdata

import de.denktmit.webapp.persistence.users.Authority
import de.denktmit.webapp.persistence.users.Group
import de.denktmit.webapp.persistence.users.RbacMapping
import de.denktmit.webapp.persistence.users.RbacRepository
import de.denktmit.webapp.persistence.testdata.Users.alicejohnson
import de.denktmit.webapp.persistence.testdata.Users.janesmith
import de.denktmit.webapp.persistence.testdata.Users.johndoe
import de.denktmit.webapp.persistence.testdata.Users.paulhiggins
import de.denktmit.webapp.persistence.testdata.Users.petergabriel

object Rbac {

    val groupAdmins = Group(10001L, "admins")
    val groupUsers = Group(10002L, "users")
    val allGroups = listOf(groupAdmins, groupUsers)

    val roleAdmin = Authority(10001L, "ADMIN")
    val roleUser = Authority(10002L, "USER")

    val groupAuthorities = mapOf(
        groupAdmins to listOf(roleAdmin, roleUser),
        groupUsers to listOf(roleUser),
    )

    val johndoeRbac = RbacMapping(
        johndoe,
        setOf(groupUsers),
        setOf(roleUser)
    )

    val janesmithRbac = RbacMapping(
        janesmith,
        setOf(groupAdmins),
        setOf(roleAdmin, roleUser)
    )

    val alicejohnsonRbac = RbacMapping(
        alicejohnson,
        setOf(groupUsers),
        setOf(roleUser)
    )

    val paulhigginsRbac = RbacMapping(
        paulhiggins,
        setOf(groupUsers),
        setOf(roleUser)
    )

    val petergabrielRbac = RbacMapping(
        petergabriel,
        setOf(groupUsers),
        setOf(roleUser)
    )

    val all = listOf(johndoeRbac, janesmithRbac, alicejohnsonRbac, paulhigginsRbac, petergabrielRbac)
    open class RepoStub(
        val data: MutableList<RbacMapping> = all.toMutableList()
    ): RbacRepository {

        override fun findOneByMail(mail: String): RbacMapping? {
            return data.find { it.user.mail == mail }
        }

        override fun findAllByMails(mails: Set<String>): List<RbacMapping> {
            return data.filter { mails.contains(it.user.mail) }
        }

        override fun setUserGroups(mail: String, groupNames: Set<String>) {
            val groups = allGroups
                .filter { groupNames.contains(it.groupName) }
                .toSet()
            val auths = groupAuthorities
                .filter { groupNames.contains(it.key.groupName) }
                .flatMap { it.value }
                .toSet()
            this.findOneByMail(mail)?.let {
                data.remove(it)
                data.add(it.copy(groups = groups, authorities = auths))
            }
        }
    }


}