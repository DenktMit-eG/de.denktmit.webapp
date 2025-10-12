package de.denktmit.webapp.webwicket.layout

import de.denktmit.wicket.components.page.DmPage
import org.apache.wicket.markup.head.IHeaderResponse
import org.apache.wicket.request.mapper.parameter.PageParameters

abstract class BasePage(
    pageParameters: PageParameters? = null,
) : DmPage(pageParameters) {

    val headerPanel: HeaderPanel = HeaderPanel()
    val footerPanel: FooterPanel = FooterPanel()

    override fun renderHead(response: IHeaderResponse) {
    }

    override fun onInitialize() {
        super.onInitialize()
        +headerPanel
        +footerPanel
    }

}