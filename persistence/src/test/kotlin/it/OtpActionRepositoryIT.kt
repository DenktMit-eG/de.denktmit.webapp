package it

import de.denktmit.webapp.persistence.otp.OtpRepository
import de.denktmit.webapp.persistence.testdata.OtpActions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class OtpActionRepositoryIT : AbstractTestBase() {

    @Autowired
    lateinit var repo: OtpRepository

    @Test
    fun testFindAll() {
        assertThat(repo.findAll()).hasSize(OtpActions.all.size)
    }

}
