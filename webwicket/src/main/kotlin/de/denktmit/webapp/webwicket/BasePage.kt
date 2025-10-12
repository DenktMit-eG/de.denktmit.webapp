package de.denktmit.webapp.webwicket

import de.denktmit.wicket.components.page.DmPage
import org.apache.wicket.markup.head.IHeaderResponse
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.request.mapper.parameter.PageParameters

abstract class BasePage(
    pageParameters: PageParameters? = null,
) : DmPage(pageParameters) {

    override fun renderHead(response: IHeaderResponse) {
    }

}
