package de.denktmit.webapp.persistence.otp

import de.denktmit.webapp.persistence.HasIdOfType
import de.denktmit.webapp.persistence.users.User
import jakarta.persistence.*
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
    @Column(name = "action_id")
    override val id: UUID = UUID.randomUUID(),

    @JoinColumn(name = "user_id")
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    val user: User,

    @Column(length = 25)
    @NotNull
    val action: String,

    @Column
    @NotNull
    val validUntil: Instant

): HasIdOfType<UUID>