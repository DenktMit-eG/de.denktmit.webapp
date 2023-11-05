package de.denktmit.webapp.persistence.testdata

import de.denktmit.webapp.persistence.Constants.FAR_FUTURE
import de.denktmit.webapp.persistence.otp.OtpAction
import de.denktmit.webapp.persistence.users.User
import java.time.Instant
import java.util.*
import kotlin.reflect.KClass

object OtpActions {

    fun createOtpAction(
        otpActionId: UUID = UUID.fromString("ad0be000-0000-4000-a000-000000000000"),
        target: KClass<out Any> = User::class,
        action: String = "delete",
        identifier: Long = 0,
        validUntil: Instant = FAR_FUTURE
    ): OtpAction {
        return OtpAction(
            otpActionId = otpActionId,
            target = target.java.canonicalName,
            action = action,
            identifier = identifier,
            validUntil = validUntil
        )
    }

    val facade00 = createOtpAction(
        UUID.fromString("facade00-0000-4000-a000-000000000000"),
        User::class,
        "activate",
        -100,
        FAR_FUTURE
    )

    val decade00 = createOtpAction(
        UUID.fromString("decade00-0000-4000-a000-000000000000"),
        User::class,
        "password-reset",
        -200,
        FAR_FUTURE
    )

    val allOtpActions = listOf(facade00, decade00)

}