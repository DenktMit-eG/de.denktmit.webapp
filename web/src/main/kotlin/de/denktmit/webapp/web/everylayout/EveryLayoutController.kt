package de.denktmit.webapp.web.everylayout

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class EveryLayoutController {

    @GetMapping("/every-layout")
    fun index(
        model: Model
    ): String {
        return "every-layout/index"
    }

}