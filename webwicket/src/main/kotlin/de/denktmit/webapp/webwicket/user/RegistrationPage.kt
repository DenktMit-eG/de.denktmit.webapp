package de.denktmit.webapp.webwicket.user

import de.denktmit.webapp.business.user.UserService
import de.denktmit.webapp.business.user.UserService.UserSavingResult
import de.denktmit.webapp.business.user.WipeableCharSequence
import de.denktmit.webapp.webwicket.layout.CenteredBasePage
import de.denktmit.wicket.components.feedback.DmFeedbackPanel
import de.denktmit.wicket.components.form.DmForm
import de.denktmit.wicket.components.form.DmPasswordTextfield
import de.denktmit.wicket.spring.bean
import org.apache.wicket.markup.html.form.EmailTextField
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.panel.FeedbackPanel
import org.apache.wicket.model.CompoundPropertyModel
import org.apache.wicket.model.Model
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.slf4j.LoggerFactory
import java.io.Serializable
import java.net.URI

class RegistrationPage(pageParameters: PageParameters?) : CenteredBasePage(pageParameters) {

    @delegate:Transient
    private val userService: UserService by bean()

    companion object {
        val LOGGER = LoggerFactory.getLogger(RegistrationPage::class.java)
    }

    override fun onInitialize() {
        super.onInitialize()

        +DmFeedbackPanel("feedback")

        val formModel = CompoundPropertyModel(Model(RegistrationFormModel("", "", "")))
        +object : DmForm<RegistrationFormModel>("registrationForm", formModel, {

            +EmailTextField("emailAddress").apply { isRequired = true }
            +DmPasswordTextfield(RegistrationFormModel::password.name) // TODO KMutableProp
            +DmPasswordTextfield(RegistrationFormModel::passwordRepeated.name).apply { isRequired = true }
        }) {
            override fun onSubmit() {
                val email = modelObject.emailAddress?.trim()
                val pwd = modelObject.password
                val pwdRep = modelObject.passwordRepeated

                if (email.isNullOrBlank()) {
                    error("E-Mail is required")
                    return
                }
                if (pwd.isNullOrBlank() || pwdRep.isNullOrBlank()) {
                    error("Password is required")
                    return
                }
                if (pwd != pwdRep) {
                    error("Passwords do not match")
                    return
                }

                val mailVerificationUri = URI.create("/validate-email")
                when (userService.createUser(email, WipeableCharSequence(pwd.toCharArray()), mailVerificationUri)) {
                    is UserSavingResult.EmailAlreadyExists -> error(getString("de.denktmit.webapp.web.validators.UniqueEmailValidator.message"))
                    is UserSavingResult.Persisted -> setResponsePage(RegistrationSuccessPage::class.java)
                    else -> error("Registration failed: ${'$'}result")
                }
            }
        }
    }
}

data class RegistrationFormModel(
    var emailAddress: String,
    var password: String,
    var passwordRepeated: String,
) : Serializable

