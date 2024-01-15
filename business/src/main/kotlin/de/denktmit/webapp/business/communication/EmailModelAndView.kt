package de.denktmit.webapp.business.communication

import org.springframework.context.i18n.LocaleContextHolder
import java.util.*

class EmailModelAndView(
    val viewName: String,
    val templateModel: Map<String, Any> = mapOf(),
    val locale: Locale,
)