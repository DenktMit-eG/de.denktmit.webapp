package de.denktmit.webapp.webwicket.user

import de.denktmit.webapp.webwicket.layout.CenteredBasePage
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.request.mapper.parameter.PageParameters

class LogoutPage(pageParameters: PageParameters?) : CenteredBasePage(pageParameters) {
    override fun onInitialize() {
        super.onInitialize()

        val form = object : Form<Void>("form") {
            override fun onSubmit() {
                // Placeholder: integrate with security to invalidate session
                info("Logout submitted")
            }
        }
        add(form)
    }
}
