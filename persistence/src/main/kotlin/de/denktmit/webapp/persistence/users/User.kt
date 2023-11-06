package de.denktmit.webapp.persistence.users

import de.denktmit.webapp.persistence.Constants.FAR_FUTURE
import de.denktmit.webapp.persistence.Constants.FAR_PAST
import de.denktmit.webapp.persistence.HasIdOfType
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.NaturalId
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
    val mail: String = "",

    @Column(length = 500)
    @NotBlank
    val password: String = "",

    @Column
    @NotNull
    val disabled: Boolean = true,

    @Column
    val lockedUntil: Instant = FAR_PAST,

    @Column
    val accountValidUntil: Instant = FAR_FUTURE,

    @Column
    val credentialsValidUntil: Instant = FAR_FUTURE,

    @Column(length = 15)
    @Enumerated(EnumType.STRING)
    @NotNull
    val role: UserRole = UserRole.USER,
): HasIdOfType<Long>