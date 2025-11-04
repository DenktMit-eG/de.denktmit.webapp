package de.denktmit.webapp.webwicket

import de.denktmit.webapp.webwicket.layout.CenteredBasePage
import de.denktmit.wicket.components.component.DmLabel
import de.denktmit.wicket.model.modelOf
import org.apache.wicket.markup.head.IHeaderResponse
import org.apache.wicket.markup.head.JavaScriptHeaderItem
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.request.resource.PackageResourceReference


class Dashboard(
    pageParameters: PageParameters?,
) : CenteredBasePage(pageParameters) {

    override fun renderHead(response: IHeaderResponse) {
        super.renderHead(response)

        response.render(
            JavaScriptHeaderItem.forReference(
                PackageResourceReference(Dashboard::class.java, "js/webapp.min.js")
            )
        )
    }
    override fun onInitialize() {
        super.onInitialize()

        +DmLabel("dataIntegrityReport", modelOf { "test" })
        +DmLabel("missingPerformanceRecords", modelOf { "test" })
        +DmLabel("invoicesCreditsFailedMailAttempts", modelOf { "test" })
        +DmLabel("unfinishedInvoices", modelOf { "test" })
        +DmLabel("unfinishedCredits", modelOf { "test" })
        +DmLabel("unpaidInvoices", modelOf { "test" })
        +DmLabel("unpaidCredits", modelOf { "test" })
    }
}