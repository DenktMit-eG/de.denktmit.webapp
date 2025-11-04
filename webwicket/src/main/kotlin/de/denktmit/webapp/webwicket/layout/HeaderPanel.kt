package de.denktmit.webapp.webwicket.layout

import de.denktmit.webapp.webwicket.user.LoginPage
import de.denktmit.webapp.webwicket.user.LogoutPage
import de.denktmit.wicket.components.base.DmContainer
import de.denktmit.wicket.components.component.DmPageLink
import de.denktmit.wicket.components.component.DmPanel
import de.denktmit.wicket.model.modelOf
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.springframework.security.core.context.SecurityContextHolder

open class HeaderPanel : DmPanel("headerPanel") {

    override fun onInitialize() {
        super.onInitialize()

        val isAuthenticated = SecurityContextHolder.getContext().authentication.name != "anonymous"

        +DmContainer("admin") {
            isVisible = isAuthenticated
        }
        +DmPageLink(
            "logout",
            if (isAuthenticated) LogoutPage::class.java else LoginPage::class.java,
            PageParameters(),
            modelOf {
                SecurityContextHolder.getContext().authentication.name
            }
        )
    }
}