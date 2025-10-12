package de.denktmit.webapp.webwicket.error

import de.denktmit.webapp.webwicket.layout.BasePage

class Http403ErrorPage : BasePage() {
    override fun onInitialize() {
        super.onInitialize()
    }

    override fun isErrorPage() = true
}
