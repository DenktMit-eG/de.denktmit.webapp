package de.denktmit.webapp.springconfig

import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.web.server.ConfigurableWebServerFactory
import org.springframework.boot.web.server.ErrorPage
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@SpringBootConfiguration
@ComponentScan("de.denktmit.webapp.web")
class WebContext : WebMvcConfigurer {

    override fun configureContentNegotiation(configurer: ContentNegotiationConfigurer) {
        configurer.favorParameter(true)
            .parameterName("mediaType")
            .ignoreAcceptHeader(true)
            .defaultContentType(MediaType.TEXT_HTML)
            .mediaType("xml", MediaType.APPLICATION_XML)
            .mediaType("json", MediaType.APPLICATION_JSON)
    }

    override fun addViewControllers(registry: ViewControllerRegistry) {
        registry.addViewController("/notFound").setViewName("forward:/notFound")
    }

    @Bean
    fun containerCustomizer(): WebServerFactoryCustomizer<ConfigurableWebServerFactory>? {
        return WebServerFactoryCustomizer { container: ConfigurableWebServerFactory ->
            container.addErrorPages(ErrorPage(HttpStatus.NOT_FOUND, "/notFound"))
            container.addErrorPages(
                ErrorPage(
                    HttpStatus.METHOD_NOT_ALLOWED,
                    "/notFound"
                )
            )
            container.addErrorPages(
                ErrorPage(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "/notFound"
                )
            )
            container.addErrorPages(ErrorPage(HttpStatus.BAD_REQUEST, "/notFound"))
        }
    }

}