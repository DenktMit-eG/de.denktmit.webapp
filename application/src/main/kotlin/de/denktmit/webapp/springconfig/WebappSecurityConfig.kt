package de.denktmit.webapp.springconfig

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.core.userdetails.User
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain


@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
class WebappSecurityConfig(
    private val securityProperties: WebappSecurityProperties,
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        if (securityProperties.method == AuthMethod.NONE) {
            http.noAuthChain()
        } else {
            http.formAuthChain()
        }
        return http.build()
    }

    private fun HttpSecurity.noAuthChain(): HttpSecurity =
        cors(CorsConfigurer<HttpSecurity>::disable)
            .csrf(CsrfConfigurer<HttpSecurity>::disable)
            .anonymous { it
                .principal(securityProperties.anonymousPrincipal)
            }
            .authorizeHttpRequests { it
                .anyRequest().permitAll()
            }

    private fun HttpSecurity.formAuthChain(): HttpSecurity =
        cors(withDefaults())
            .csrf(withDefaults())
            .formLogin(withDefaults())
            .authorizeHttpRequests { it
                    // Allow access to static resources. Not that everything under /static/folder will be served
                    // from path /folder.
                    .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
                    // Landing page or other public pages
                    .requestMatchers("/").permitAll()
                    // Admin-specific rules
                    .requestMatchers("/admin").hasRole("admin")
                    // All other pages
                    .anyRequest().authenticated()
            }


    @Bean
    fun userDetailsService(): InMemoryUserDetailsManager {
        return InMemoryUserDetailsManager(
            User.withDefaultPasswordEncoder()
                .username("admin")
                .password("password")
                .roles("admin")
                .build()
        )
    }


}