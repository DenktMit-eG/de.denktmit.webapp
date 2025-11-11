package de.denktmit.webapp.webwicket.error

import de.denktmit.webapp.webwicket.layout.CenteredBasePage

class Http403ErrorPage : CenteredBasePage() {
    override fun onInitialize() {
        super.onInitialize()
    }

    override fun isErrorPage() = true
}
