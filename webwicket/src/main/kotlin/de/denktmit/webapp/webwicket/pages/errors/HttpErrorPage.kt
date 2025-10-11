package de.denktmit.webapp.webwicket.pages.errors

import de.denktmit.webapp.webwicket.pages.BasePage
import de.denktmit.wicket.components.component.DmLabel
import de.denktmit.wicket.components.q

open class HttpErrorPage(
  private val errorMessage: String,
  private val description: String = "",
) : BasePage() {
  override fun onInitialize() {
    super.onInitialize()
      q(DmLabel("error", errorMessage))
      q(DmLabel("description", description))
  }

  override fun isErrorPage() = true

  class Http404ErrorPage : HttpErrorPage("404 - Seite nicht gefunden")
}
