package it

import de.denktmit.webapp.persistence.testdata.Users
import de.denktmit.webapp.persistence.users.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class UserRepositoryIT : AbstractTestBase() {

    @Autowired
    lateinit var repo: UserRepository

    @Test
    fun testFindAll() {
        assertThat(repo.findAll()).hasSize(Users.all.size)
    }

}
