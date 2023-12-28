package de.denktmit.webapp.springconfig

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver
import org.thymeleaf.templatemode.TemplateMode


@ComponentScan("de.denktmit.webapp.business")
@Configuration
@EnableConfigurationProperties(
    BusinessContextConfigProperties::class,
)
class BusinessConfiguration {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }

    @Bean
    fun textTemplateResolver(applicationContext: ApplicationContext): SpringResourceTemplateResolver {
        val templateResolver = SpringResourceTemplateResolver()
        templateResolver.setApplicationContext(applicationContext)
        templateResolver.order = 0
        templateResolver.resolvablePatterns = setOf("text/*")
        templateResolver.prefix = "classpath:/mails/"
        templateResolver.suffix = ".txt"
        templateResolver.templateMode = TemplateMode.TEXT
        templateResolver.characterEncoding = Charsets.UTF_8.name()
        templateResolver.isCacheable = false
        templateResolver.checkExistence = true
        return templateResolver
    }

    @Bean
    fun htmlTemplateResolver(applicationContext: ApplicationContext): SpringResourceTemplateResolver {
        val templateResolver = SpringResourceTemplateResolver()
        templateResolver.setApplicationContext(applicationContext)
        templateResolver.order = 1
        templateResolver.resolvablePatterns = setOf("html/*")
        templateResolver.prefix = "classpath:/mails/"
        templateResolver.suffix = ".html"
        templateResolver.templateMode = TemplateMode.HTML
        templateResolver.characterEncoding = Charsets.UTF_8.name()
        templateResolver.isCacheable = false
        templateResolver.checkExistence = true
        return templateResolver
    }

}