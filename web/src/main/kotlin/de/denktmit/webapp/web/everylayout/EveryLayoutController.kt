package de.denktmit.webapp.web.everylayout

import de.denktmit.webapp.business.user.UserService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class EveryLayoutController(
    private val userService: UserService
) {

    @GetMapping("/every-layout")
    fun index(
        model: Model
    ): String {
        return "every-layout/index"
    }

}