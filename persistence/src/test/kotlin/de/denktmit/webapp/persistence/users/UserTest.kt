package de.denktmit.webapp.persistence.users

import de.denktmit.webapp.persistence.testdata.Users
import de.denktmit.webapp.persistence.testdata.Users.johndoe
import nl.jqno.equalsverifier.EqualsVerifier
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class UserTest {

    @Test
    fun testEquals() {
        EqualsVerifier.forClass(User::class.java).verify()
    }

    @Test
    fun testToString() {
        Assertions.assertThat(Users.johndoe.toString()).isEqualTo("User(mail='user1.johndoe@example.com')")
    }

    @Test
    fun defaultCreate() {
        Assertions.assertThat(User.create("mail", "password")).isNotNull
    }

    @Test
    fun copy() {
        val copy = johndoe.copy()
        Assertions.assertThat(copy).isEqualTo(johndoe)
        Assertions.assertThat(copy).isNotSameAs(johndoe)
    }
}