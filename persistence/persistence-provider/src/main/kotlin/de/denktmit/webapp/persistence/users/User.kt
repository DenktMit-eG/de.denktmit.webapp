package de.denktmit.webapp.persistence.users

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.Instant
import java.util.UUID

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
    val enabled: Boolean = false,

    @Column(length = 15)
    @Enumerated(EnumType.STRING)
    @NotNull
    val role: UserRole = UserRole.USER,
)