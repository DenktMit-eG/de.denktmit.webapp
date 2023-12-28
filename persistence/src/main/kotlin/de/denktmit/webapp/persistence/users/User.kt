package de.denktmit.webapp.persistence.users

import de.denktmit.webapp.persistence.Constants.FAR_FUTURE
import de.denktmit.webapp.persistence.Constants.FAR_PAST
import de.denktmit.webapp.persistence.HasIdOfType
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.NaturalId
import org.hibernate.annotations.TimeZoneStorage
import org.hibernate.annotations.TimeZoneStorageType
import java.time.Instant
import java.util.*

@Entity
@Table(name = "users")
data class User(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    override val id: Long = 0,

    @NaturalId
    @Column(length = 255)
    @NotBlank
    val mail: String,

    @Column(length = 500)
    @NotBlank
    val password: String,

    @Column
    @NotNull
    val disabled: Boolean,

    @Column
    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    val lockedUntil: Instant,

    @Column
    val accountValidUntil: Instant,

    @Column
    val credentialsValidUntil: Instant,

): HasIdOfType<Long> {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is User) return false

        if (id != other.id) return false
        return mail == other.mail
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + mail.hashCode()
        return result
    }
}