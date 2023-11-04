package it

import de.denktmit.webapp.persistence.otp.OtpRepository
import de.denktmit.webapp.persistence.users.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class OtpActionRepositoryIT : AbstractTestBase() {

    @Autowired
    lateinit var repo: OtpRepository

    @Test
    fun testFindAll() {
        assertThat(repo.findAll()).hasSize(2)
    }

}
