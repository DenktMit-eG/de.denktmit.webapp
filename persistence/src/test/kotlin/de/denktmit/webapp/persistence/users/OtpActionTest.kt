package de.denktmit.webapp.persistence.users

import de.denktmit.webapp.persistence.testdata.OtpActions.facade00
import de.denktmit.webapp.persistence.testdata.Users.johndoe
import nl.jqno.equalsverifier.EqualsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Duration


class OtpActionTest {

    @Test
    fun testEquals() {
        EqualsVerifier.forClass(OtpAction::class.java).verify()
    }

    @Test
    fun testToString() {
        assertThat(facade00.toString()).isEqualTo("OtpAction(token=facade00-0000-4000-a000-000000000000, action='verify-email')")
    }

    @Test
    fun defaultCreate() {
        assertThat(OtpAction.createOtpAction(johndoe, "junit", Duration.ZERO)).isNotNull
    }

    @Test
    fun copy() {
        val copy = facade00.copy()
        assertThat(copy).isEqualTo(facade00)
        assertThat(copy).isNotSameAs(facade00)
    }
}