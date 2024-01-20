package it

import de.denktmit.webapp.persistence.users.Group
import de.denktmit.webapp.persistence.users.RbacRepository
import de.denktmit.webapp.persistence.testdata.Rbac
import de.denktmit.webapp.persistence.testdata.Rbac.all
import de.denktmit.webapp.persistence.testdata.Rbac.groupAdmins
import de.denktmit.webapp.persistence.testdata.Rbac.groupUsers
import de.denktmit.webapp.testutils.softAssert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

@IntegrationTestConfiguration
class RbacRepositoryIT {

    @Autowired
    lateinit var repo: RbacRepository
    lateinit var repoStub: RbacRepository

    @BeforeEach
    fun setUp() {
        repoStub = Rbac.RepoStub()
    }

    @Test
    fun testFindOneByMail() {
        all.forEach { rbacMapping ->
            val result = repo.findOneByMail(rbacMapping.user.mail)
            softAssert {
                assertThat(result).isNotNull
                assertThat(result).isEqualTo(repoStub.findOneByMail(rbacMapping.user.mail))
            }
        }
    }

    @Test
    fun testFindAllByMails() {
        val mails = all.map { it.user.mail }.toSet()
        val result = repo.findAllByMails(mails)
        softAssert {
            assertThat(result).isNotEmpty
            assertThat(result).containsExactlyInAnyOrder(*repoStub.findAllByMails(mails).toTypedArray())
        }
    }

    @Test
    @Transactional
    fun testSetUserGroupsToAdmin() {
        verifyUserGroupUpdates(groupAdmins)
    }

    @Test
    @Transactional
    fun testSetUserGroupsToUser() {
        verifyUserGroupUpdates(groupUsers)
    }

    private fun verifyUserGroupUpdates(newGroup: Group) {
        val notAdmins = all.filter { !it.groups.contains(newGroup) }
        notAdmins.forEach { rbacMapping ->
            // Setup actual
            val resultBefore = repo.findOneByMail(rbacMapping.user.mail)
            repo.setUserGroups(rbacMapping.user.mail, setOf(newGroup.groupName))
            val resultAfter = repo.findOneByMail(rbacMapping.user.mail)

            // Setup expected
            val expectedBefore = repoStub.findOneByMail(rbacMapping.user.mail)
            repoStub.setUserGroups(rbacMapping.user.mail, setOf(newGroup.groupName))
            val expectedAfter = repo.findOneByMail(rbacMapping.user.mail)

            softAssert {
                assertThat(resultBefore).isNotNull
                assertThat(resultAfter).isNotNull
                assertThat(resultBefore).isNotEqualTo(resultAfter)
                assertThat(resultBefore).isEqualTo(expectedBefore)
                assertThat(resultAfter).isEqualTo(expectedAfter)
            }
        }
    }

}
