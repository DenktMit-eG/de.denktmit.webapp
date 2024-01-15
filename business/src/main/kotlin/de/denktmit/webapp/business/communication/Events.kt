package de.denktmit.webapp.business.communication

import de.denktmit.webapp.persistence.users.User
import org.springframework.context.ApplicationEvent
import java.net.URI
import java.time.Instant
import java.util.*

class EmailValidationRequestedEvent(
    val user: User,
    val mailVerificationUri: URI,
    val validUntil: Instant,
    val locale: Locale
): ApplicationEvent(user)

class InvitationFiledEvent(
    val user: User,
    val invitationAcceptUri: URI,
    val validUntil: Instant,
    val locale: Locale
): ApplicationEvent(user)

class PasswordRecoveryRequestedEvent(
    val user: User,
    val passwordResetUri: URI,
    val validUntil: Instant,
    val locale: Locale
): ApplicationEvent(user)