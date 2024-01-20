package de.denktmit.webapp.business.user

import de.denktmit.webapp.business.user.UserService.UserSavingResult
import de.denktmit.webapp.persistence.users.OtpAction
import de.denktmit.webapp.persistence.users.User
import java.net.URI
import java.time.Duration
import java.time.Instant
import java.util.*

interface OtpSecuredActionsService {
    fun isTokenValid(token: UUID, action: String, instant: Instant = Instant.now()): Boolean

    fun createOtpAction(user: User, action: String, duration: Duration): OtpAction

    //fun createOtpCallbackUri(otpAction: OtpAction, baseUri: URI): String

//    fun processUserCreatedEvent(event: UserCreatedEvent)

    fun validateEmailAddress(token: UUID, instant: Instant = Instant.now()): OtpActionResult<UserSavingResult, TokenNotResolved>

//    fun processUserInvitedEvent(event: UserInvitedEvent)

//    fun sendInvitationMail(user: UserEntity, invitationAcceptUri: URI)
    fun acceptInvitation(token: UUID, newPassword: WipeableCharSequence, instant: Instant = Instant.now()): OtpActionResult<UserSavingResult, TokenNotResolved>

    fun startPasswordRecovery(mail: String, passwordResetUri: URI)

    fun resetPassword(token: UUID, newPassword: WipeableCharSequence, instant: Instant = Instant.now()): OtpActionResult<UserSavingResult, TokenNotResolved>

}

