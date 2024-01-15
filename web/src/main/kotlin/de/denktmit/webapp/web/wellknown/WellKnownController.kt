package de.denktmit.webapp.web.wellknown

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/.well-known")
class WellKnownController {

    @GetMapping("/change-password")
    fun getHome(): String {
        return "redirect:/me"
    }

}