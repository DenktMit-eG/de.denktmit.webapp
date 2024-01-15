package de.denktmit.webapp.persistence.testdata

import de.denktmit.webapp.persistence.Constants.FAR_FUTURE
import de.denktmit.webapp.persistence.otp.OtpAction
import de.denktmit.webapp.persistence.otp.OtpRepository
import de.denktmit.webapp.persistence.testdata.Users.janesmith
import de.denktmit.webapp.persistence.testdata.Users.johndoe
import de.denktmit.webapp.persistence.testdata.Users.petergabriel
import de.denktmit.webapp.persistence.users.User
import java.time.Instant
import java.util.*

object OtpActions {

    fun createOtpAction(
        actionId: Long = 0,
        token: UUID = UUID.fromString("ad0be000-0000-4000-a000-000000000000"),
        user: User = Users.createUser(),
        action: String = "delete",
        validUntil: Instant = FAR_FUTURE
    ): OtpAction {
        return OtpAction(
            id = actionId,
            token = token,
            user = user,
            action = action,
            validUntil = validUntil
        )
    }

    val facade00 = createOtpAction(
        -100,
        UUID.fromString("facade00-0000-4000-a000-000000000000"),
        johndoe,
        "verify-email",
        FAR_FUTURE
    )

    val decade00 = createOtpAction(
        -200,
        UUID.fromString("decade00-0000-4000-a000-000000000000"),
        janesmith,
        "reset-password",
        FAR_FUTURE
    )

    val adobe000 = createOtpAction(
        -300,
        UUID.fromString("ad0be000-0000-4000-a000-000000000000"),
        petergabriel,
        "accept-invite",
        FAR_FUTURE
    )

    val all = listOf(facade00, decade00, adobe000)

    open class RepoStub(
        val data: MutableList<OtpAction> = all.toMutableList()
    ): CrudRepositoryStub<OtpAction, Long>(data), OtpRepository {

        override fun existsByTokenAndActionAndValidUntilAfter(token: UUID, action: String, instant: Instant): Boolean {
            return this.findOneByTokenAndActionAndValidUntilAfter(token, action, instant) != null
        }

        override fun findOneByTokenAndActionAndValidUntilAfter(token: UUID, action: String, validationTimestamp: Instant): OtpAction? {
            return data.firstOrNull { otpAction -> otpAction.token == token && otpAction.action == action && otpAction.validUntil.isAfter(validationTimestamp) }
        }

        override fun deleteByUserMailAndAction(mail: String, action: String): Int {
            val items = data.filter { otpAction -> otpAction.user.mail == mail && otpAction.action == action }
            data.removeAll(items)
            return items.size
        }

    }

}