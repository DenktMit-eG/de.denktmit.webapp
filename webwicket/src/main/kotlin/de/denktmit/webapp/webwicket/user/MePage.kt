package de.denktmit.webapp.webwicket.user

import de.denktmit.webapp.webwicket.layout.CenteredBasePage
import de.denktmit.wicket.components.feedback.DmFeedbackPanel
import de.denktmit.wicket.components.form.DmForm
import de.denktmit.wicket.components.form.DmPasswordTextfield
import org.apache.wicket.model.CompoundPropertyModel
import org.apache.wicket.model.Model
import org.apache.wicket.request.mapper.parameter.PageParameters
import java.io.Serializable

class MePage(pageParameters: PageParameters?) : CenteredBasePage(pageParameters) {

    data class ChangePasswordFormModel(
        var oldPassword: String? = null,
        var password: String? = null,
        var passwordRepeated: String? = null,
    ) : Serializable

    override fun onInitialize() {
        super.onInitialize()

        +DmFeedbackPanel("feedback")

        val formModel = CompoundPropertyModel(Model(ChangePasswordFormModel()))
        +DmForm<ChangePasswordFormModel>("form", formModel) {

            +DmPasswordTextfield("oldPassword") { isRequired = true }
            +DmPasswordTextfield("password") { isRequired = true }
            +DmPasswordTextfield("passwordRepeated") { isRequired = true }
            onSubmit = fun() {
                // Placeholder; validate repeat matches
                if (modelObject.password != modelObject.passwordRepeated) {
                    error("Passwords do not match")
                } else {
                    info("Password change submitted")
                }
            }
        }
    }
}
