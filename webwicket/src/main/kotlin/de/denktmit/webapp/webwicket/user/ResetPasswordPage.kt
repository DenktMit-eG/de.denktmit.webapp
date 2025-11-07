package de.denktmit.webapp.webwicket.user

import de.denktmit.webapp.webwicket.layout.CenteredBasePage
import de.denktmit.wicket.components.base.DmContainer
import de.denktmit.wicket.components.feedback.DmFeedbackPanel
import de.denktmit.wicket.components.form.DmForm
import org.apache.wicket.markup.html.form.PasswordTextField
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

        +DmFeedbackPanel("feedback")

        val isValid = token?.isNotBlank() == true

        // Headline texts visibility
        +DmContainer("tokenValidText") { isVisible = isValid }
        +DmContainer("tokenInvalidText") { isVisible = !isValid }

        // Containers for content
        +DmContainer("formContainer") {
            isVisible = isValid

            val formModel = CompoundPropertyModel(Model(PasswordResetFormModel()))
            +DmForm<PasswordResetFormModel>("form", formModel) {

                +PasswordTextField("password").apply { isRequired = true }
                +PasswordTextField("passwordRepeated").apply { isRequired = true }
                onSubmit = fun() {
                    if (modelObject.password != modelObject.passwordRepeated) {
                        error("Passwords do not match")
                    } else {
                        info("Password reset submitted")
                    }
                }
            }
        }
        +DmContainer("infoContainer") { isVisible = !isValid }
    }
}
