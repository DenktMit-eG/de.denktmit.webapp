package de.denktmit.webapp.web.user

import de.denktmit.webapp.business.user.*
import de.denktmit.webapp.business.user.OtpActionResult
import de.denktmit.webapp.business.user.UserService.UserSavingResult
import de.denktmit.webapp.business.user.UserService.UserSavingResult.EmailAlreadyExists
import de.denktmit.webapp.business.user.UserService.UserSavingResult.Persisted
import de.denktmit.webapp.springconfig.WebContextProperties
import de.denktmit.webapp.web.user.internal.PasswordResetFormData
import de.denktmit.webapp.web.user.internal.RegistrationFormData
import de.denktmit.webapp.web.user.internal.UniqueEmailMessageCode
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.util.*


@Controller
class RegistrationController(
    private val properties: WebContextProperties,
    private val userService: UserService,
    private val otpService: OtpSecuredActionsService,
) {

    @GetMapping("/registration")
    fun registrationFormGet(model: Model): String {
        model.addAttribute("registrationForm", RegistrationFormData())
        return "registration/form"
    }

    @PostMapping("/registration")
    fun registrationFormPost(
        @Validated @ModelAttribute("registrationForm") formModel: RegistrationFormData,
        bindingResult: BindingResult,
    ): String {
        if (bindingResult.hasErrors() || registerUser(formModel, bindingResult) !is Persisted) {
            return "registration/form"
        }
        return "redirect:/registration-success"
    }

    private fun registerUser(formModel: RegistrationFormData, bindingResult: BindingResult): UserSavingResult {
        val mailVerificationUri = properties.baseUri.resolve("/validate-email")
        val result = userService.createUser(
            formModel.emailAddress,
            WipeableCharSequence(formModel.password),
            mailVerificationUri
        )
        if (result is EmailAlreadyExists) {
            bindingResult.rejectValue(formModel::emailAddress.name, UniqueEmailMessageCode)
        }
        return result
    }

    @GetMapping("/registration-success")
    fun registrationSuccess(): String {
        return "registration/success"
    }

    @GetMapping("/invite-accept")
    fun inviteAcceptFormGet(@RequestParam(name = "token") token: UUID, model: Model): String {
        val tokenValid = otpService.isTokenValid(token, OtpSecuredActionsServiceImpl.ACTION_ACCEPT_INVITE)
        model.addAttribute("isTokenValid", tokenValid)
        model.addAttribute("passwordResetForm", PasswordResetFormData())
        return "registration/acceptInvitation"
    }

    @PostMapping("/invite-accept")
    fun inviteAcceptFormPost(
        @RequestParam(name = "token") token: UUID,
        @Validated @ModelAttribute("passwordResetForm") formModel: PasswordResetFormData,
        bindingResult: BindingResult,
        model: Model,
        redirectAttributes: RedirectAttributes,
    ): String {
        if (!otpService.isTokenValid(token, OtpSecuredActionsServiceImpl.ACTION_ACCEPT_INVITE)) {
            redirectAttributes.addAttribute("token", token)
            return "redirect:/invite-accept"
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("isTokenValid", true)
            return "registration/acceptInvitation"
        }
        val otpActionResult = otpService
            .acceptInvitation(token, WipeableCharSequence(formModel.password))
        val inviteAccepted = otpActionResult is OtpActionResult.Success
                && otpActionResult.payload is Persisted

        if (!inviteAccepted) {
            throw RuntimeException("An unexpected error occurred, the invitation could not be processed with result $otpActionResult")
        }
        redirectAttributes.addFlashAttribute("acceptInvitationSucceeded", true)
        return "redirect:login"
    }

}