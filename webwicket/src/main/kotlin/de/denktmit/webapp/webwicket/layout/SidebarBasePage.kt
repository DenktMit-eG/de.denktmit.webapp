package de.denktmit.webapp.webwicket.layout

import org.apache.wicket.request.mapper.parameter.PageParameters

abstract class SidebarBasePage(
    pageParameters: PageParameters? = null,
    val sidebarPanel: SidebarPanel = SidebarPanel()
) : BasePage(pageParameters) {

    override fun onInitialize() {
        super.onInitialize()
        +sidebarPanel
    }

}