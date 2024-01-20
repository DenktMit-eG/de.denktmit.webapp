package it

import de.denktmit.webapp.jooq.generated.tables.references.OTP_ACTIONS
import de.denktmit.webapp.persistence.Constants.FAR_FUTURE
import de.denktmit.webapp.persistence.users.OtpAction
import de.denktmit.webapp.persistence.users.OtpRepository
import de.denktmit.webapp.persistence.setBindParameterValues
import de.denktmit.webapp.persistence.testdata.OtpActions
import de.denktmit.webapp.persistence.testdata.OtpActions.all
import de.denktmit.webapp.persistence.testdata.OtpActions.facade00
import de.denktmit.webapp.persistence.testdata.Users.janesmith
import de.denktmit.webapp.persistence.testdata.Users.johndoe
import de.denktmit.webapp.testutils.softAssert
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions.assertThat
import org.jooq.util.postgres.PostgresDSL
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.Instant
import java.util.*

@IntegrationTestConfiguration
class OtpActionRepositoryIT {

    @Autowired
    lateinit var repo: OtpRepository
    lateinit var repoStub: OtpActions.RepoStub

    @Autowired
    lateinit var em: EntityManager

    @BeforeEach
    fun setUp() {
        repoStub = OtpActions.RepoStub()
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
    @Transactional
    fun testCreateNew() {
        val result =  repo.save(
            OtpAction.createOtpAction(
            janesmith,
            "junit-action",
            Duration.ofHours(1),
            token = UUID.fromString("c0c0a000-0000-4000-a000-000000000000")
        ))
        val resultStub =  repoStub.save(
            OtpAction.createOtpAction(
            janesmith,
            "junit-action",
            Duration.ofHours(1),
            token = UUID.fromString("c0c0a000-0000-4000-a000-000000000000")
        ))
        assertThat(result).isNotNull
        assertThat(result).isEqualTo(resultStub)
    }

    @Test
    fun testFindAllWithJooq() {
        val jooqQuery = PostgresDSL.select(*OTP_ACTIONS.fields()).from(OTP_ACTIONS)
        val queryString: String = jooqQuery.sql
        val jpaQuery = em.createNativeQuery(queryString, OtpAction::class.java).setBindParameterValues(jooqQuery)
        @Suppress("UNCHECKED_CAST") val actions: List<OtpAction> = jpaQuery.resultList as List<OtpAction>
        assertThat(actions).containsExactlyInAnyOrder(*OtpActions.all.toTypedArray())
    }

    @Test
    fun testExistsByIdAndActionAndValidUntilAfterSucceeds() {
        all.forEach { otpAction ->
            validateExistsByIdAndActionAndValidUntilAfter(otpAction, FAR_FUTURE.minusSeconds(1), true)
        }
    }

    @Test
    fun testExistsByIdAndActionAndValidUntilAfterFails() {
        all.forEach { otpAction ->
            validateExistsByIdAndActionAndValidUntilAfter(otpAction, FAR_FUTURE, false)
            validateExistsByIdAndActionAndValidUntilAfter(
                otpAction.copy(token = UUID.fromString("07f53bd2-d4b6-4d68-9356-0621e0833c48")),
                FAR_FUTURE.minusSeconds(1),
                false
            )
            validateExistsByIdAndActionAndValidUntilAfter(
                otpAction.copy(action = "undefined"),
                FAR_FUTURE.minusSeconds(1),
                false
            )
        }
    }

    private fun validateExistsByIdAndActionAndValidUntilAfter(otpAction: OtpAction, validationTime: Instant, expectedOutcome: Boolean) {
        val result = repo.existsByTokenAndActionAndValidUntilAfter(otpAction.token, otpAction.action, validationTime)
        val stubResult = repoStub.existsByTokenAndActionAndValidUntilAfter(otpAction.token, otpAction.action, validationTime)
        softAssert {
            assertThat(result).isEqualTo(expectedOutcome)
            assertThat(result).isEqualTo(stubResult)
        }
    }

    @Test
    fun testValidateFindOneByTokenAndActionAndValidUntilAfter() {
        all.forEach { otpAction ->
            validateFindOneByTokenAndActionAndValidUntilAfter(otpAction, FAR_FUTURE.minusSeconds(1), true)
            validateFindOneByTokenAndActionAndValidUntilAfter(otpAction, FAR_FUTURE, false)
            validateFindOneByTokenAndActionAndValidUntilAfter(
                otpAction.copy(token = UUID.fromString("07f53bd2-d4b6-4d68-9356-0621e0833c48")),
                FAR_FUTURE.minusSeconds(1),
                false
            )
            validateFindOneByTokenAndActionAndValidUntilAfter(
                otpAction.copy(action = "undefined"),
                FAR_FUTURE.minusSeconds(1),
                false
            )
        }
    }

    private fun validateFindOneByTokenAndActionAndValidUntilAfter(action: OtpAction, validationTimestamp: Instant, expectNotNullResult: Boolean) {
        val result = repo.findOneByTokenAndActionAndValidUntilAfter(action.token, action.action, validationTimestamp)
        val stubResult = repoStub.findOneByTokenAndActionAndValidUntilAfter(action.token, action.action, validationTimestamp)
        softAssert {
            if (expectNotNullResult) assertThat(result).isNotNull else assertThat(result).isNull()
            assertThat(result).isEqualTo(stubResult)
        }
    }

    @Test
    @Transactional
    fun testDeleteByUserMailAndAction() {
        val rowsDeleted = repo.deleteByUserMailAndAction(johndoe.mail, facade00.action)
        val stubRowsDeleted = repoStub.deleteByUserMailAndAction(johndoe.mail, facade00.action)
        val remainingRows = repo.findAll()
        val remainingStubRows = repoStub.findAll()
        softAssert {
            assertThat(remainingRows).isNotEmpty
            assertThat(rowsDeleted).isEqualTo(stubRowsDeleted)
            assertThat(remainingRows).containsExactlyInAnyOrder(*remainingStubRows.toList().toTypedArray())
        }

    }

}
