package de.denktmit.webapp.webwicket.error

import de.denktmit.webapp.webwicket.layout.CenteredBasePage
import de.denktmit.webapp.webwicket.utils.exceptionMessages
import de.denktmit.wicket.components.q
import org.apache.wicket.markup.html.basic.MultiLineLabel
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.slf4j.LoggerFactory

class GenericErrorPage(
    val exception: Exception?,
    pageParameters: PageParameters?,
) : CenteredBasePage(pageParameters) {
    public constructor(pageParameters: PageParameters) : this(null, pageParameters)

    companion object {
        private val logger = LoggerFactory.getLogger(GenericErrorPage::class.java)
    }

    override fun onInitialize() {
        super.onInitialize()

        logger.error(exception?.stackTraceToString())
        q(MultiLineLabel("error", exception?.exceptionMessages(true) ?: "Unerwarteter Fehler"))
    }

    override fun isErrorPage() = true
}
