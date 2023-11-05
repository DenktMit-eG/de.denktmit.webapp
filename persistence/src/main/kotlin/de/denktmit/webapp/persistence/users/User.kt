package de.denktmit.webapp.persistence.users

import de.denktmit.webapp.persistence.Constants.FAR_FUTURE
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.Instant



@Entity
@Table(name = "users")
data class User(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val userId: Long = -1,

    @Column(length = 255)
    @NotBlank
    val mail: String = "",

    @Column(length = 60)
    @NotBlank
    val password: String = "",

    @Column(length = 60)
    @NotBlank
    val firstName: String = "",

    @Column(length = 60)
    @NotBlank
    val lastName: String = "",

    @Column
    @NotNull
    val disabled: Boolean = true,

    @Column
    val lockedUntil: Instant? = null,

    @Column
    val accountValidUntil: Instant = FAR_FUTURE,

    @Column
    val credentialsValidUntil: Instant = FAR_FUTURE,

    @Column(length = 15)
    @Enumerated(EnumType.STRING)
    @NotNull
    val role: UserRole = UserRole.USER,
)