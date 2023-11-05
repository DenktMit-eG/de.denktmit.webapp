package de.denktmit.webapp.web

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/")
class HomeController {

    @GetMapping
    fun getHome(): String {
        return "layout_main"
    }

}