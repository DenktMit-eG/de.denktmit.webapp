package de.denktmit.webapp.webwicket.pages

import org.apache.wicket.markup.head.IHeaderResponse
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.request.mapper.parameter.PageParameters

abstract class BasePage(
    pageParameters: PageParameters? = null,
) : WebPage(pageParameters) {

    override fun renderHead(response: IHeaderResponse) {
    }

}
