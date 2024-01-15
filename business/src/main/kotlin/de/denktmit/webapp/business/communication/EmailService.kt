package de.denktmit.webapp.business.communication

import de.denktmit.webapp.springconfig.BusinessContextConfigProperties
import org.slf4j.LoggerFactory
import org.springframework.context.MessageSource
import org.springframework.mail.MailException
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.thymeleaf.context.Context
import org.thymeleaf.spring6.SpringTemplateEngine

@Service
class EmailService(
    private val config: BusinessContextConfigProperties,
    private val mailSender: JavaMailSender,
    private val emailTemplateEngine: SpringTemplateEngine,
    override val messageSource: MessageSource
): CommunicationService {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(EmailService::class.java)
    }

    @Async
    override fun sendEmail(recipients: Array<String>, subject: String, modelAndView: EmailModelAndView) {
        val mailBody = emailTemplateEngine.process(modelAndView.viewName, Context(modelAndView.locale, modelAndView.templateModel))
        val email = SimpleMailMessage().apply {
            from = config.mail.sender
            setTo(*recipients)
            setSubject(subject)
            text = mailBody
        }
        try {
            mailSender.send(email)
            LOGGER.info("Sent email with subject '$subject' to recipients '${recipients.joinToString()}'")
        } catch (e: MailException) {
            LOGGER.error("Failed to send email with subject '$subject' to users '${recipients.joinToString()}'", e)
        }
    }


}