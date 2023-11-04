package it

import de.denktmit.webapp.persistence.users.User
import de.denktmit.webapp.persistence.users.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class UserRepositoryIT : AbstractTestBase() {

    @Autowired
    lateinit var repo: UserRepository

    @Test
    fun testFindAll() {
        assertThat(repo.findAll()).hasSize(2)
    }

}
