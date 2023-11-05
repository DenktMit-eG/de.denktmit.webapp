package de.denktmit.webapp.web

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HomeController {

    @GetMapping(value = ["/"])
    fun getHome(): String {
        return "layout_main"
    }

}