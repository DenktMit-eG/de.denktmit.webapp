package de.denktmit.webapp.persistence.rbac

import de.denktmit.webapp.persistence.testdata.Rbac
import nl.jqno.equalsverifier.EqualsVerifier
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class RbacMappingTest {

    @Test
    fun testEquals() {
        EqualsVerifier.forClass(RbacMapping::class.java).verify()
    }

    @Test
    fun testToString() {
        Assertions.assertThat(Rbac.janesmithRbac.toString()).isEqualTo("RbacMapping(user=User(mail='admin2.janesmith@example.com'), groups=[Group(groupName='admins')], authorities=[Authority(authority='ADMIN'), Authority(authority='USER')])")
    }
}