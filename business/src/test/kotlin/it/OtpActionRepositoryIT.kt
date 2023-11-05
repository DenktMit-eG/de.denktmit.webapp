package it

import de.denktmit.webapp.business.security.OtpRepository
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
