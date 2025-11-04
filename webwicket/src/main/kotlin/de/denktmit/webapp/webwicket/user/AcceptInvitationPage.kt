package de.denktmit.webapp.webwicket.user

import de.denktmit.webapp.business.user.*
import de.denktmit.webapp.webwicket.layout.CenteredBasePage
import de.denktmit.wicket.components.base.DmContainer
import de.denktmit.wicket.components.feedback.DmFeedbackPanel
import de.denktmit.wicket.components.form.DmForm
import de.denktmit.wicket.components.form.DmPasswordTextfield
import de.denktmit.wicket.spring.bean
import org.apache.wicket.model.CompoundPropertyModel
import org.apache.wicket.model.Model
import org.apache.wicket.request.mapper.parameter.PageParameters
import java.io.Serializable
import java.util.*

class AcceptInvitationPage(pageParameters: PageParameters?) : CenteredBasePage(pageParameters) {


    @delegate:Transient
    private val otpService: OtpSecuredActionsService by bean()

    private val token: UUID? = try {
        pageParameters?.get("token")?.toOptionalString()?.let { UUID.fromString(it) }
    } catch (_: Exception) {
        null
    }

    data class PasswordResetFormModel(
        var password: String? = null,
        var passwordRepeated: String? = null,
    ) : Serializable

    override fun onInitialize() {
        super.onInitialize()

        +DmFeedbackPanel("feedback")

        val isValid = token?.let { otpService.isTokenValid(it, OtpSecuredActionsServiceImpl.ACTION_ACCEPT_INVITE) } == true

        +DmContainer("tokenValidText") { isVisible = isValid }
        +DmContainer("tokenInvalidText") { isVisible = !isValid }

        +DmContainer("formContainer") {
            isVisible = isValid

            val formModel = CompoundPropertyModel(Model(PasswordResetFormModel()))
            +object : DmForm<PasswordResetFormModel>("form", formModel, {

                +DmPasswordTextfield("password") { isRequired = true }
                +DmPasswordTextfield("passwordRepeated").apply { isRequired = true }
            }) {
                override fun onSubmit() {
                    val pwd = modelObject.password
                    val pwdRep = modelObject.passwordRepeated
                    if (pwd.isNullOrBlank() || pwdRep.isNullOrBlank()) {
                        error("Password is required")
                        return
                    }
                    if (pwd != pwdRep) {
                        error("Passwords do not match")
                        return
                    }
                    val tok = token ?: run {
                        error("Invalid token")
                        return
                    }
                    val result = otpService.acceptInvitation(tok, WipeableCharSequence(pwd.toCharArray()))
                    val inviteAccepted = result is OtpActionResult.Success && result.payload is UserService.UserSavingResult.Persisted
                    if (!inviteAccepted) {
                        error("An unexpected error occurred, the invitation could not be processed with result ${'$'}result")
                        return
                    }
                    val params = PageParameters().add("acceptInvitationSucceeded", "true")
                    setResponsePage(LoginPage::class.java, params)
                }
            }
        }
        +DmContainer("infoContainer") { isVisible = !isValid }
    }
}
