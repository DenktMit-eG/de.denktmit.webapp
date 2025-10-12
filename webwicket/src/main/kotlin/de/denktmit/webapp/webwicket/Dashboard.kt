package de.denktmit.webapp.webwicket

import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.request.mapper.parameter.PageParameters

class Dashboard(
    pageParameters: PageParameters?,
) : BasePage(pageParameters) {
  override fun onInitialize() {
    super.onInitialize()
      add(WebMarkupContainer("dataIntegrityReport"))
      add(WebMarkupContainer("missingPerformanceRecords"))
      add(WebMarkupContainer("invoicesCreditsFailedMailAttempts"))
      add(WebMarkupContainer("unfinishedInvoices"))
      add(WebMarkupContainer("unfinishedCredits"))
      add(WebMarkupContainer("unpaidInvoices"))
      add(WebMarkupContainer("unpaidCredits"))
  }
}