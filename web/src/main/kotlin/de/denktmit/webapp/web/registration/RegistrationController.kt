package de.denktmit.webapp.web.registration

import de.denktmit.webapp.business.security.WebappUserManagementService
import de.denktmit.webapp.business.security.WipeableCharSequence
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class RegistrationController(
    private val userService: WebappUserManagementService
) {

    @GetMapping("/registration")
    fun showRegistrationForm(model: Model): String {
        model.addAttribute("registrationForm", RegistrationFormData())
        return "registration/form"
    }

    @PostMapping("/registration")
    fun createNewUser(
        @Validated @ModelAttribute("registrationForm") formModel: RegistrationFormData,
        bindingResult: BindingResult,
        model: Model
    ): String {
        if (bindingResult.hasErrors()) {
            // If there are validation errors, return to the form page
            return "registration/form"
        }
        userService.createUser(formModel.emailAddress, WipeableCharSequence(formModel.password))
        return "redirect:/"
    }

}