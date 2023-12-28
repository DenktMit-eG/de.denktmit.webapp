package de.denktmit.webapp.business.user

import de.denktmit.webapp.persistence.users.UserEntity
import org.springframework.context.ApplicationEvent
import java.net.URI
import java.util.Locale

/**
 * Event is triggered, after a new user was created
 */
class UserCreatedEvent(val user: UserEntity, val mailVerificationUri: URI, val locale: Locale): ApplicationEvent(user)

/**
 * Event is triggered, after an invited user was created
 */
class UserInvitedEvent(val user: UserEntity, val invitationAcceptUri: URI, val locale: Locale): ApplicationEvent(user)