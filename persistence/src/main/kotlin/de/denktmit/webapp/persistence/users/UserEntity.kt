package de.denktmit.webapp.persistence.users

import de.denktmit.webapp.persistence.Constants
import de.denktmit.webapp.persistence.HasIdOfType
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.NaturalId
import java.time.Instant

@Entity
@Table(name = "users")
data class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    override val id: Long = 0,
    @NaturalId
    @Column(length = 255)
    @get:NotBlank
    val mail: String,
    @get:NotNull
    val mailVerified: Boolean,
    @Column(length = 500)
    @get:NotBlank
    val password: String,
    @Column
    @get:NotNull
    val disabled: Boolean,
    @Column
    val lockedUntil: Instant,
    @Column
    val accountValidUntil: Instant,
    @Column
    val credentialsValidUntil: Instant,
): HasIdOfType<Long> {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserEntity

        return mail == other.mail
    }

    override fun hashCode(): Int {
        return mail.hashCode()
    }

    override fun toString(): String {
        return "User(mail='$mail')"
    }

    companion object {
        fun create(
            mail: String,
            password: String,
            userId: Long = 0,
            mailVerified: Boolean = false,
            disabled: Boolean = false,
            lockedUntil: Instant = Constants.FAR_PAST,
            accountValidUntil: Instant = Constants.FAR_FUTURE,
            credentialsValidUntil: Instant = Constants.FAR_FUTURE,
        ): UserEntity {
            return UserEntity(
                id = userId,
                mail = mail,
                mailVerified = mailVerified,
                password = password,
                disabled = disabled,
                lockedUntil = lockedUntil,
                accountValidUntil = accountValidUntil,
                credentialsValidUntil = credentialsValidUntil,
            )
        }
    }
}