package de.denktmit.webapp.web.registration

import de.denktmit.webapp.business.user.UserService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class RegistrationController(
    private val userService: UserService
) {

    @GetMapping("/registration")
    fun getHome(): String {
        Integer.parseInt("I am not a number")
        return "index"
    }

}