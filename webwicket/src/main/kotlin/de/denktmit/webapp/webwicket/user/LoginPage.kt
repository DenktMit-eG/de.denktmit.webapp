package de.denktmit.webapp.webwicket.user

import de.denktmit.webapp.webwicket.layout.CenteredBasePage
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.form.EmailTextField
import org.apache.wicket.markup.html.form.PasswordTextField
import org.apache.wicket.markup.html.panel.FeedbackPanel
import org.apache.wicket.model.CompoundPropertyModel
import org.apache.wicket.model.Model
import org.apache.wicket.request.mapper.parameter.PageParameters
import java.io.Serializable

class LoginPage(pageParameters: PageParameters?) : CenteredBasePage(pageParameters) {

    data class LoginFormModel(
        var email: String? = null,
        var password: String? = null
    ) : Serializable

    override fun onInitialize() {
        super.onInitialize()

        val feedback = FeedbackPanel("feedback")
        add(feedback)

        val formModel = CompoundPropertyModel(Model(LoginFormModel()))
        val form = object : Form<LoginFormModel>("form", formModel) {
            override fun onSubmit() {
                // Placeholder: integrate with security later
                info("Login attempted for ${'$'}{modelObject.email}")
            }
        }
        add(form)

        val emailField = EmailTextField("email")
        emailField.isRequired = true
        form.add(emailField)

        val passwordField = PasswordTextField("password")
        passwordField.isRequired = true
        form.add(passwordField)
    }
}
