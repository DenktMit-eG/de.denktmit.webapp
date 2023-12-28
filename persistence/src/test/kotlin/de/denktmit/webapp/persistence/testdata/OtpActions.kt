package de.denktmit.webapp.persistence.testdata

import de.denktmit.webapp.persistence.Constants.FAR_FUTURE
import de.denktmit.webapp.persistence.otp.OtpAction
import de.denktmit.webapp.persistence.otp.OtpRepository
import de.denktmit.webapp.persistence.testdata.Users.janesmith
import de.denktmit.webapp.persistence.testdata.Users.johndoe
import de.denktmit.webapp.persistence.users.User
import java.time.Instant
import java.util.*
import kotlin.reflect.KClass

object OtpActions {

    fun createOtpAction(
        otpActionId: UUID = UUID.fromString("ad0be000-0000-4000-a000-000000000000"),
        user: User = Users.createUser(),
        action: String = "delete",
        validUntil: Instant = FAR_FUTURE
    ): OtpAction {
        return OtpAction(
            id = otpActionId,
            user = user,
            action = action,
            validUntil = validUntil
        )
    }

    val facade00 = createOtpAction(
        UUID.fromString("facade00-0000-4000-a000-000000000000"),
        johndoe,
        "verify-email",
        FAR_FUTURE
    )

    val decade00 = createOtpAction(
        UUID.fromString("decade00-0000-4000-a000-000000000000"),
        janesmith,
        "reset-password",
        FAR_FUTURE
    )

    val all = listOf(facade00, decade00)

    open class RepoStub(
        data: MutableList<OtpAction> = all.toMutableList()
    ): CrudRepositoryStub<OtpAction, UUID>(data), OtpRepository

}