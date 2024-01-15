package de.denktmit.webapp.persistence.rbac

import de.denktmit.webapp.persistence.testdata.Rbac
import nl.jqno.equalsverifier.EqualsVerifier
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class GroupTest {

    @Test
    fun testEquals() {
        EqualsVerifier.forClass(Group::class.java).verify()
    }

    @Test
    fun testToString() {
        Assertions.assertThat(Rbac.groupUsers.toString()).isEqualTo("Group(groupName='users')")
    }
}