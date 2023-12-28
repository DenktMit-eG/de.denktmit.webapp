package de.denktmit.webapp.business.user

import de.denktmit.webapp.business.communication.EmailValidationRequestedEvent
import de.denktmit.webapp.business.communication.InvitationFiledEvent
import de.denktmit.webapp.springconfig.BusinessContextConfigProperties
import org.springframework.context.ApplicationEventPublisher
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionalEventListener

@Service
class UserEventHandler(
    private val config: BusinessContextConfigProperties,
    private val publisher: ApplicationEventPublisher,
    private val otpActionService: OtpSecuredActionsService
) {


    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    fun processUserCreatedEvent(event: UserCreatedEvent) {
        if (!event.user.mailVerified) {
            val otpAction = otpActionService.createOtpAction(event.user, OtpSecuredActionsServiceImpl.ACTION_VERIFY_EMAIL, config.verifyMailDuration)
            val callbackUrl = otpAction.createOtpCallbackUri(event.mailVerificationUri)
            publisher.publishEvent(EmailValidationRequestedEvent(event.user, callbackUrl, otpAction.validUntil, event.locale))
        }
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    fun processUserInvitedEvent(event: UserInvitedEvent) {
        val otpAction = otpActionService.createOtpAction(event.user, OtpSecuredActionsServiceImpl.ACTION_ACCEPT_INVITE, config.acceptInviteDuration)
        val callbackUri = otpAction.createOtpCallbackUri(event.invitationAcceptUri)
        publisher.publishEvent(InvitationFiledEvent(event.user, callbackUri, otpAction.validUntil, event.locale))
    }

}