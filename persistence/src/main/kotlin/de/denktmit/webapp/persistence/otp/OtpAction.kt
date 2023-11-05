package de.denktmit.webapp.persistence.otp

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.UuidGenerator
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "otp_actions")
data class OtpAction(

    @Id
    @GeneratedValue
    @UuidGenerator
    val otpActionId: UUID = UUID.randomUUID(),

    @Column(length = 255)
    @NotBlank
    val target: String = "",

    @Column(length = 25)
    @NotBlank
    val action: String = "",

    @Column
    @NotBlank
    val identifier: Long = 0,

    @Column
    @NotNull
    val validUntil: Instant = Instant.MIN,
)