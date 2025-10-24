package de.denktmit.webapp.webwicket.user

import de.denktmit.webapp.webwicket.layout.CenteredBasePage
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.form.PasswordTextField
import org.apache.wicket.markup.html.panel.FeedbackPanel
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

        val feedback = FeedbackPanel("feedback")
        add(feedback)

        val formModel = CompoundPropertyModel(Model(ChangePasswordFormModel()))
        val form = object : Form<ChangePasswordFormModel>("form", formModel) {
            override fun onSubmit() {
                // Placeholder; validate repeat matches
                if (modelObject.password != modelObject.passwordRepeated) {
                    error("Passwords do not match")
                } else {
                    info("Password change submitted")
                }
            }
        }
        add(form)

        val oldPwd = PasswordTextField("oldPassword").apply { isRequired = true }
        val pwd = PasswordTextField("password").apply { isRequired = true }
        val pwdRep = PasswordTextField("passwordRepeated").apply { isRequired = true }
        form.add(oldPwd)
        form.add(pwd)
        form.add(pwdRep)
    }
}
