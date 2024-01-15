package de.denktmit.webapp.business.user

import de.denktmit.webapp.persistence.users.User
import org.springframework.context.ApplicationEvent
import java.net.URI
import java.util.*

/**
 * Event is triggered, after a new user was created
 */
class UserCreatedEvent(val user: User, val mailVerificationUri: URI, val locale: Locale): ApplicationEvent(user)

/**
 * Event is triggered, after an invited user was created
 */
class UserInvitedEvent(val user: User, val invitationAcceptUri: URI, val locale: Locale): ApplicationEvent(user)