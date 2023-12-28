package de.denktmit.webapp.springconfig

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder

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

}