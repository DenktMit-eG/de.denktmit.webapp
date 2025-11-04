package de.denktmit.webapp.springconfig

import de.denktmit.webapp.webwicket.filters.RedirectAuthenticatedUsersFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
class WebappSecurityConfig(
    private val securityProperties: WebappSecurityProperties,
    private val redirectAuthenticatedUsersFilter: RedirectAuthenticatedUsersFilter,
) {

    /**
     * Allow access to static resources. Not that everything under /static/folder will be served from path /folder.
     */
    private val staticAssetsPaths = arrayOf(
        "/css/**",
        "/js/**",
        "/img/**"
    )

    /**
     * Allow access to pages that a public on purpose, like e.g. landing pages, error pages, and legal pages
     */
    private val publicPagePaths = arrayOf(
        "/", "/index.html",
        "/error", "/error.html",
        "/every-layout", "/every-layout.html",
        "/.well-known/*",
        "/registration*",
        "/recover-password*",
        "/validate-email*",
        "/invite-accept*",
        "/dashboard*",
    )

    /**
     * Allow access to utility pages e.g. to register new user, validate emails or recover passwords
     */
    private val utilityPagePaths = arrayOf(
        "/user/login",
        "/actuator/**",// TODO actuator über einen eigenen port laufen lassen -> dann kann der cluster sich die daten im pod ziehen ohne dass andere darauf kommen
    )

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        if (securityProperties.method == AuthMethod.NONE) {
            noAuthChain(http)
        } else {
            formAuthChain(http)
        }
        return http.build()
    }

    private fun noAuthChain(httpSecurity: HttpSecurity): HttpSecurity = httpSecurity
        .cors(CorsConfigurer<HttpSecurity>::disable)
        .csrf(CsrfConfigurer<HttpSecurity>::disable)
        .anonymous { anonymousConfigurer ->
            anonymousConfigurer
                .principal(securityProperties.anonymousPrincipal)
                .authorities("ROLE_ANON")
        }
        .authorizeHttpRequests { authorizeConfigurer ->
            authorizeConfigurer.anyRequest().permitAll()
        }

    private fun formAuthChain(httpSecurity: HttpSecurity): HttpSecurity = httpSecurity
        .addFilterBefore(redirectAuthenticatedUsersFilter, UsernamePasswordAuthenticationFilter::class.java)
        .cors(CorsConfigurer<HttpSecurity>::disable)
        .csrf(CsrfConfigurer<HttpSecurity>::disable)
        .formLogin { formLoginConfigurer ->
            formLoginConfigurer
                .loginPage("/user/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/me", true)
                .usernameParameter("email")
                .passwordParameter("password")
                .permitAll()
        }.logout { logoutConfigurer ->
            logoutConfigurer
                .logoutUrl("/logout")
                .logoutSuccessUrl("/user/login")
        }
        .authorizeHttpRequests { authorizationConfigurer ->
            authorizationConfigurer
                .requestMatchers(*staticAssetsPaths, *publicPagePaths, *utilityPagePaths).permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
        }
        .anonymous { anonymousConfigurer ->
            anonymousConfigurer
                .principal(securityProperties.anonymousPrincipal)
                .authorities("ROLE_ANON")
        }

}