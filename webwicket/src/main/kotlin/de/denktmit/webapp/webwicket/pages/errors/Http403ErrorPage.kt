package de.denktmit.webapp.webwicket.pages.errors

import de.denktmit.webapp.webwicket.pages.BasePage

class Http403ErrorPage : BasePage() {
  override fun onInitialize() {
    super.onInitialize()
  }

  override fun isErrorPage() = true
}
