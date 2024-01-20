package de.denktmit.webapp.persistence.users

import de.denktmit.webapp.persistence.testdata.Rbac
import nl.jqno.equalsverifier.EqualsVerifier
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class AuthorityTest {

    @Test
    fun testEquals() {
        EqualsVerifier.forClass(Authority::class.java).verify()
    }

    @Test
    fun testToString() {
        Assertions.assertThat(Rbac.roleAdmin.toString()).isEqualTo("Authority(authority='ADMIN')")
    }

}