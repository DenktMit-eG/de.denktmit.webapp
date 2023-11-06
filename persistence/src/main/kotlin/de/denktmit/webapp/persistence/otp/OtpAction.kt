package de.denktmit.webapp.persistence.otp

import de.denktmit.webapp.persistence.HasIdOfType
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.UuidGenerator
import java.time.Instant
import java.util.*

@Entity
@Table(name = "otp_actions")
data class OtpAction(

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "otp_action_id")
    override val id: UUID = UUID.randomUUID(),

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
): HasIdOfType<UUID>