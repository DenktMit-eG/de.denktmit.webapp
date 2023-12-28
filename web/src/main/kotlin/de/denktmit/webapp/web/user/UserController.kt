package de.denktmit.webapp.web.user

import de.denktmit.webapp.business.user.OtpSecuredActionsService
import de.denktmit.webapp.business.user.OtpActionResult
import de.denktmit.webapp.business.user.OtpSecuredActionsServiceImpl
import de.denktmit.webapp.business.user.UserService
import de.denktmit.webapp.business.user.UserService.UserSavingResult.Persisted
import de.denktmit.webapp.business.user.WipeableCharSequence
import de.denktmit.webapp.web.user.internal.PasswordUpdateFormData
import org.springframework.security.core.Authentication
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
class UserController(
    private val userService: UserService,
    private val otpService: OtpSecuredActionsService,
) {

    @GetMapping("/me")
    fun showMyProfile(model: Model): String {
        model.addAttribute("passwordUpdateForm", PasswordUpdateFormData())
        return "user/me"
    }

    @PostMapping("/me")
    fun createNewUser(
        @Validated @ModelAttribute("passwordUpdateForm") formModel: PasswordUpdateFormData,
        bindingResult: BindingResult,
        model: RedirectAttributes,
        authentication: Authentication
    ): String {
        if (bindingResult.hasErrors()) {
            return "user/me"
        }
        val oldPassword = WipeableCharSequence(formModel.oldPassword)
        val newPassword = WipeableCharSequence(formModel.password)
        val result = userService.updatePassword(authentication, oldPassword, newPassword)
        model.addFlashAttribute("changePasswordResult", result is Persisted)
        return "redirect:/me"
    }

    @GetMapping("/validate-email")
    fun validateEmail(@RequestParam(name = "token") token: UUID, model: RedirectAttributes): String {
        if (!otpService.isTokenValid(token, OtpSecuredActionsServiceImpl.ACTION_VERIFY_EMAIL)) {
            model.addFlashAttribute("emailValidationResult", false)
            return "redirect:/me"
        }
        val otpActionResult = otpService.validateEmailAddress(token)
        val emailValidated = otpActionResult is OtpActionResult.Success
                && otpActionResult.payload is Persisted
        model.addFlashAttribute("emailValidationResult", emailValidated)
        return "redirect:/me"
    }

}