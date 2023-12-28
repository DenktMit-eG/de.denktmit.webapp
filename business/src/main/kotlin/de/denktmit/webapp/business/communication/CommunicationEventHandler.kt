package de.denktmit.webapp.business.communication

import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.util.*

@Service
class CommunicationEventHandler(
    private val communicationService: CommunicationService
) {

    @Async
    @EventListener
    fun processUserCreatedEvent(event: EmailValidationRequestedEvent) {
        val emailModelAndView = EmailModelAndView(
            communicationService.i18nView("mails.emailverification.viewfile", event.locale),
            mapOf(
                "name" to event.user.mail,
                "registrationDate" to Date(),
                "tokenUrl" to event.mailVerificationUri,
                "tokenValidUntil" to event.validUntil,
            ),
            event.locale
        )
        communicationService.sendEmail(
            arrayOf(event.user.mail),
            communicationService.i18nSubject("mails.emailverification.subject", locale = event.locale),
            emailModelAndView
        )
    }

    @Async
    @EventListener
    fun sendInvitationMail(event: InvitationFiledEvent) {
        val emailModelAndView = EmailModelAndView(
            communicationService.i18nView("mails.invitation.viewfile", event.locale),
            mapOf(
                "name" to event.user.mail,
                "registrationDate" to Date(),
                "tokenUrl" to event.invitationAcceptUri,
                "tokenValidUntil" to event.validUntil,
            ),
            event.locale
        )
        communicationService.sendEmail(
            arrayOf(event.user.mail),
            communicationService.i18nSubject("mails.invitation.subject", locale = event.locale),
            emailModelAndView
        )
    }

    @Async
    @EventListener
    fun sendPasswordRecoveryMail(event: PasswordRecoveryRequestedEvent) {
        val emailModelAndView = EmailModelAndView(
            communicationService.i18nView("mails.passwordrecovery.viewfile", event.locale),
            mapOf(
                "name" to event.user.mail,
                "registrationDate" to Date(),
                "tokenUrl" to event.passwordResetUri,
                "tokenValidUntil" to event.validUntil,
            ),
            event.locale
        )
        communicationService.sendEmail(
            arrayOf(event.user.mail),
            communicationService.i18nSubject("mails.passwordrecovery.subject", locale = event.locale),
            emailModelAndView
        )
    }

}