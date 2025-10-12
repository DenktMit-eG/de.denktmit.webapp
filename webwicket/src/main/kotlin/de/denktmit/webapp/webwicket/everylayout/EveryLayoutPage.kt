package de.denktmit.webapp.webwicket.everylayout

import de.denktmit.webapp.webwicket.layout.SidebarBasePage
import org.apache.wicket.request.mapper.parameter.PageParameters

class EveryLayoutPage(
    pageParameters: PageParameters?,
) : SidebarBasePage(pageParameters, EveryLayoutSidebarPanel()) {

    override fun onInitialize() {
        super.onInitialize()
        sidebarPanel.isVisible = true
    }

}