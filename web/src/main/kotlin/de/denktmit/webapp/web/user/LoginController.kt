package de.denktmit.webapp.web.user

import de.denktmit.webapp.business.user.OtpActionResult
import de.denktmit.webapp.business.user.OtpSecuredActionsService
import de.denktmit.webapp.business.user.OtpSecuredActionsServiceImpl.Companion.ACTION_RESET_PASSWORD
import de.denktmit.webapp.business.user.UserService
import de.denktmit.webapp.business.user.WipeableCharSequence
import de.denktmit.webapp.springconfig.WebContextProperties
import de.denktmit.webapp.web.user.internal.PasswordResetFormData
import de.denktmit.webapp.web.user.internal.PasswordUpdateFormData
import jakarta.validation.constraints.Email
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
class LoginController(
    private val properties: WebContextProperties,
    private val otpService: OtpSecuredActionsService,
) {
    @GetMapping("/login", "/login.html")
    fun login(): String {
        return "user/login"
    }

    @GetMapping("/login-error", "/login-error.html")
    fun loginError(model: Model): String {
        model.addAttribute("loginError", true);
        return "user/login"
    }

    @GetMapping("/logout", "/logout.html")
    fun logout(): String {
        return "user/logout"
    }

    @PostMapping("/recover-password")
    fun startPasswordRecovery(
        @Validated @Email @ModelAttribute("recoverEmail") recoverEmail: String,
        model: RedirectAttributes,
    ): String {
        val passwordResetUri = properties.baseUri.resolve("/recover-password-reset")
        otpService.startPasswordRecovery(recoverEmail, passwordResetUri)
        model.addFlashAttribute("passwordRecoveryStarted", true)
        return "redirect:/login"
    }

    @GetMapping("/recover-password-reset")
    fun validateEmail(@RequestParam(name = "token") token: UUID, model: Model): String {
        val tokenValid = otpService.isTokenValid(token, ACTION_RESET_PASSWORD)
        model.addAttribute("isTokenValid", tokenValid)
        model.addAttribute("passwordResetForm", PasswordUpdateFormData())
        return "user/resetPassword"
    }

    @PostMapping("/recover-password-reset")
    fun createNewUser(
        @RequestParam(name = "token") token: UUID,
        @Validated @ModelAttribute("passwordResetForm") formModel: PasswordResetFormData,
        bindingResult: BindingResult,
        model: Model,
        redirectAttributes: RedirectAttributes,
    ): String {
        if (!otpService.isTokenValid(token, ACTION_RESET_PASSWORD)) {
            redirectAttributes.addAttribute("token", token)
            return "redirect:/recover-password-reset"
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("isTokenValid", true)
            return "user/resetPassword"
        }
        val otpActionResult = otpService
            .resetPassword(token, WipeableCharSequence(formModel.password))
        val inviteAccepted = otpActionResult is OtpActionResult.Success
                && otpActionResult.payload is UserService.UserSavingResult.Persisted
        if (!inviteAccepted) {
            throw RuntimeException("An unexpected error occurred, the invitation could not be processed with result $otpActionResult")
        }
        redirectAttributes.addFlashAttribute("passwordRecoverySucceeded", true)
        return "redirect:login"
    }

}