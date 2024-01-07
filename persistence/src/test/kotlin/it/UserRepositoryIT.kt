package it

import de.denktmit.webapp.persistence.testdata.Users
import de.denktmit.webapp.persistence.users.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTestConfiguration
class UserRepositoryIT {

    @Autowired
    lateinit var repo: UserRepository

    @Test
    fun testFindAll() {
        val users = repo.findAll()
        assertThat(users).containsExactlyInAnyOrder(*Users.all.toTypedArray())
    }

}
