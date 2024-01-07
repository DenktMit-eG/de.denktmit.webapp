package de.denktmit.webapp.web.admin

import de.denktmit.webapp.business.user.UserService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class UserAdminController(
    private val userService: UserService
) {

    @GetMapping("/admin/users")
    fun getHome(
        model: Model
    ): String {
        val dataTable = userService.userData()
        model.addAttribute("dataTable", dataTable)
        return "admin/users"
    }

}