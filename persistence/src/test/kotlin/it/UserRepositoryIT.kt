package it

import de.denktmit.webapp.persistence.testdata.Users
import de.denktmit.webapp.persistence.testdata.Users.all
import de.denktmit.webapp.persistence.users.UserRepository
import de.denktmit.webapp.testutils.softAssert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTestConfiguration
class UserRepositoryIT {

    @Autowired
    lateinit var repo: UserRepository
    lateinit var repoStub: UserRepository

    @BeforeEach
    fun setUp() {
        repoStub = Users.RepoStub()
    }

    @Test
    fun testFindAll() {
        val result = repo.findAll()
        softAssert {
            assertThat(result).isNotEmpty
            assertThat(result).containsExactlyInAnyOrder(*repoStub.findAll().toList().toTypedArray())
        }

    }

    @Test
    fun findOneByMail() {
        all.forEach { user ->
            val result = repo.findOneByMail(user.mail)
            softAssert {
                assertThat(result).isNotNull
                assertThat(result).isEqualTo(repoStub.findOneByMail(user.mail))
            }
        }
    }

    @Test
    fun findOneByMailReturnsNull() {
        val result = repo.findOneByMail("undefined")
        softAssert {
            assertThat(result).isNull()
            assertThat(result).isEqualTo(repoStub.findOneByMail("undefined"))
        }
    }

    @Test
    fun existsByMailTrue() {
        all.forEach { user ->
            val result = repo.existsByMail(user.mail)
            softAssert {
                assertThat(result).isTrue
                assertThat(result).isEqualTo(repoStub.existsByMail(user.mail))
            }
        }
    }

    @Test
    fun existsByMailReturnsFalse() {
        val result = repo.existsByMail("undefined")
        softAssert {
            assertThat(result).isFalse
            assertThat(result).isEqualTo(repoStub.existsByMail("undefined"))
        }
    }

}
