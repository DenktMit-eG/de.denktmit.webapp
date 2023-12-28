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
        assertThat(repo.findAll()).containsExactlyInAnyOrder(*Users.all.toTypedArray())
    }

}
