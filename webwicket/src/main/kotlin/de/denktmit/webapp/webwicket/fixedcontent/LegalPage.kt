package de.denktmit.webapp.webwicket.fixedcontent

import de.denktmit.webapp.webwicket.BasePage
import de.denktmit.wicket.components.base.DmContainer
import de.denktmit.wicket.components.page.DmPage
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.request.mapper.parameter.PageParameters

class LegalPage(
    pageParameters: PageParameters?,
) : BasePage(pageParameters) {


  override fun onInitialize() {
    super.onInitialize()
    +DmContainer("content").apply {
      renderBodyOnly = true
    }
  }

}