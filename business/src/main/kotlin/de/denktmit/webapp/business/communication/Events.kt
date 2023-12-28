package de.denktmit.webapp.business.communication

import de.denktmit.webapp.persistence.users.UserEntity
import org.springframework.context.ApplicationEvent
import java.net.URI
import java.time.Instant
import java.util.Locale

class EmailValidationRequestedEvent(
    val user: UserEntity,
    val mailVerificationUri: URI,
    val validUntil: Instant,
    val locale: Locale
): ApplicationEvent(user)

class InvitationFiledEvent(
    val user: UserEntity,
    val invitationAcceptUri: URI,
    val validUntil: Instant,
    val locale: Locale
): ApplicationEvent(user)

class PasswordRecoveryRequestedEvent(
    val user: UserEntity,
    val passwordResetUri: URI,
    val validUntil: Instant,
    val locale: Locale
): ApplicationEvent(user)