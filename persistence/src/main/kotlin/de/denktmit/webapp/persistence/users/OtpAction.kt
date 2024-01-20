package de.denktmit.webapp.persistence.users

import de.denktmit.webapp.persistence.HasIdOfType
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.NaturalId
import java.net.URI
import java.time.Duration
import java.time.Instant
import java.util.*

@Entity
@Table(name = "otp_actions")
data class OtpAction(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "action_id")
    override val id: Long = 0,

    @NaturalId
    @Column(name = "token")
    val token: UUID,

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

): HasIdOfType<Long> {

    companion object {
        fun createOtpAction(
            user: User,
            action: String,
            duration: Duration,
            actionId: Long = 0,
            token: UUID = UUID.randomUUID(),
        ): OtpAction {
            return OtpAction(
                id = actionId,
                token = token,
                user = user,
                action = action,
                validUntil = Instant.now().plus(duration)
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OtpAction

        return token == other.token
    }

    override fun hashCode(): Int {
        return token.hashCode()
    }

    override fun toString(): String {
        return "OtpAction(token=$token, action='$action')"
    }

    fun createOtpCallbackUri(baseUri: URI, tokenParamName: String = "token"): URI {
        val query = baseUri.query?.replace("(^|&)paramName(=[^&]*)?".toRegex(), "")
            ?.replace("&+", "&")
            ?.replace("^&", "")
        val newQuery = if (!query.isNullOrBlank()) "$query&$tokenParamName=$token" else "$tokenParamName=$token"
        return URI(
            baseUri.scheme,
            baseUri.authority,
            baseUri.path,
            newQuery,
            baseUri.fragment
        )
    }

}