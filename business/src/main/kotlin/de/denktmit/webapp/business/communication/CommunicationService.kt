package de.denktmit.webapp.business.communication

import org.springframework.context.MessageSource
import java.util.*

interface CommunicationService {

    val messageSource: MessageSource

    fun i18nSubject(key: String, args: Array<Any> = emptyArray(), defaultMessage: String? = null, locale: Locale): String {
        return messageSource.getMessage(key, args, defaultMessage, locale) ?: ""
    }

    fun i18nView(key: String, locale: Locale): String {
        return messageSource.getMessage(key, emptyArray(), locale)
    }

    fun sendEmail(recipients: Array<String>, subject: String, modelAndView: EmailModelAndView)

}