package it

import de.denktmit.webapp.jooq.generated.tables.references.OTP_ACTIONS
import de.denktmit.webapp.persistence.otp.OtpAction
import de.denktmit.webapp.persistence.otp.OtpRepository
import de.denktmit.webapp.persistence.setBindParameterValues
import de.denktmit.webapp.persistence.testdata.OtpActions
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions.assertThat
import org.jooq.util.postgres.PostgresDSL
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import jakarta.persistence.Query as JpaQuery
import org.jooq.Query as JooqQuery

class OtpActionRepositoryIT : AbstractTestBase() {

    @Autowired
    lateinit var repo: OtpRepository

    @Autowired
    lateinit var em: EntityManager

    @Test
    fun testFindAll() {
        assertThat(repo.findAll()).containsExactlyInAnyOrder(*OtpActions.all.toTypedArray())
    }

    @Test
    fun testFindAllWithJooq() {
        val jooqQuery = PostgresDSL.select(*OTP_ACTIONS.fields()).from(OTP_ACTIONS)
        val queryString: String = jooqQuery.sql
        val jpaQuery = em.createNativeQuery(queryString, OtpAction::class.java).setBindParameterValues(jooqQuery)
        @Suppress("UNCHECKED_CAST") val actions: List<OtpAction> = jpaQuery.resultList as List<OtpAction>
        assertThat(actions).containsExactlyInAnyOrder(*OtpActions.all.toTypedArray())
    }



}
