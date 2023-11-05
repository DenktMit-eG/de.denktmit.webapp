package de.denktmit.webapp.springconfig

import de.denktmit.webapp.business.security.WebappUserDetailService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val myUserDetailService: WebappUserDetailService,
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { authz ->
                authz
                    .requestMatchers("/api/admin/**").hasRole("ADMIN")
                    .requestMatchers("/api/user/**").hasRole("USER")
                    .requestMatchers("/").anonymous()
                    .anyRequest().authenticated()
            }
            .formLogin { formLogin -> formLogin
                .usernameParameter("username") // default is username
                .passwordParameter("password") // default is password
//                .loginPage("/login") // default is /login with an HTTP get
//                .failureUrl("/login?error") // default is /login?error
//                .loginProcessingUrl("/login"); // default is /login
            }

        return http.build()
    }

    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer {
        return WebSecurityCustomizer { web ->
            web.ignoring().requestMatchers("/static/**")
        }
    }


}