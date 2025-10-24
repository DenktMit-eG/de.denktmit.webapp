package de.denktmit.webapp.webwicket.user

import de.denktmit.webapp.webwicket.layout.CenteredBasePage
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.form.PasswordTextField
import org.apache.wicket.markup.html.panel.FeedbackPanel
import org.apache.wicket.model.CompoundPropertyModel
import org.apache.wicket.model.Model
import org.apache.wicket.request.mapper.parameter.PageParameters
import java.io.Serializable

class ResetPasswordPage(pageParameters: PageParameters?) : CenteredBasePage(pageParameters) {

    private val token: String? = pageParameters?.get("token")?.toOptionalString()

    data class PasswordResetFormModel(
        var password: String? = null,
        var passwordRepeated: String? = null,
    ) : Serializable

    override fun onInitialize() {
        super.onInitialize()

        val feedback = FeedbackPanel("feedback")
        add(feedback)

        val isValid = token?.isNotBlank() == true

        // Headline texts visibility
        val tokenValidText = WebMarkupContainer("tokenValidText").apply { isVisible = isValid }
        val tokenInvalidText = WebMarkupContainer("tokenInvalidText").apply { isVisible = !isValid }
        add(tokenValidText)
        add(tokenInvalidText)

        // Containers for content
        val formContainer = WebMarkupContainer("formContainer").apply { isVisible = isValid }
        val infoContainer = WebMarkupContainer("infoContainer").apply { isVisible = !isValid }
        add(formContainer)
        add(infoContainer)

        val formModel = CompoundPropertyModel(Model(PasswordResetFormModel()))
        val form = object : Form<PasswordResetFormModel>("form", formModel) {
            override fun onSubmit() {
                if (modelObject.password != modelObject.passwordRepeated) {
                    error("Passwords do not match")
                } else {
                    info("Password reset submitted")
                }
            }
        }
        formContainer.add(form)

        val pwd = PasswordTextField("password").apply { isRequired = true }
        val pwdRep = PasswordTextField("passwordRepeated").apply { isRequired = true }
        form.add(pwd)
        form.add(pwdRep)
    }
}
