package de.denktmit.webapp.web.staticpages

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.server.ResponseStatusException
import java.io.IOException
import java.util.*


@Controller
@RequestMapping("/p")
class StaticPageController(
    private val resourceLoader: ResourceLoader,
    @Value("\${spring.mvc.view.suffix:html}")
    private val viewSuffix: String
) {

    @RequestMapping("/{pathVariable}")
    fun handleRequest(@PathVariable pathVariable: String, locale: Locale): String {
        val normalizedPathVariable = if (pathVariable.endsWith(".$viewSuffix")) {
            pathVariable
        } else {
            "$pathVariable.$viewSuffix"
        }
        val viewPath = "classpath:/templates/fixed-content/$normalizedPathVariable"
        if (resourceExists(viewPath)) {
            return "fixed-content/$normalizedPathVariable"
        } else {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }
    }

    private fun resourceExists(resourcePath: String): Boolean {
        return try {
            val resource: Resource = resourceLoader.getResource(resourcePath)
            resource.exists()
        } catch (e: IOException) {
            false
        }
    }

}