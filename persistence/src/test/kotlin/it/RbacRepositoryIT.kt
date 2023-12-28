package it

import de.denktmit.webapp.persistence.rbac.RbacRepository
import de.denktmit.webapp.persistence.testdata.Rbac.janeSmithRbac
import de.denktmit.webapp.persistence.testdata.Users.janesmith
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTestConfiguration
class RbacRepositoryIT {

    @Autowired
    lateinit var repo: RbacRepository

    @Test
    fun testFindAll() {
        assertThat(repo.findByMail(janesmith.mail)).isEqualTo(janeSmithRbac)
    }

}
