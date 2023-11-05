package de.denktmit.webapp.springconfig

import org.springframework.boot.SpringBootConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.http.MediaType
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@SpringBootConfiguration
@ComponentScan("de.denktmit.webapp.web", "de.denktmit.webapp.admin")
class WebContext : WebMvcConfigurer {

    override fun configureContentNegotiation(configurer: ContentNegotiationConfigurer) {
        configurer
            .favorParameter(false)
            .parameterName("format")
            .ignoreAcceptHeader(true)
            .defaultContentType(MediaType.TEXT_HTML)
            .mediaType("xml", MediaType.APPLICATION_XML)
            .mediaType("json", MediaType.APPLICATION_JSON)
    }

}