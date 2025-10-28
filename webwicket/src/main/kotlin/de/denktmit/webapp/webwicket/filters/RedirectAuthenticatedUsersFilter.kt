package de.denktmit.webapp.webwicket.filters

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

@Component
class RedirectAuthenticatedUsersFilter(
    private val redirectDefinitions: Map<String, String> = mapOf(
        "/p/user/login" to "/p/me",
        "/p/user/registration" to "/p/me",
    )
) : OncePerRequestFilter() {
    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authentication = SecurityContextHolder.getContext().authentication
        val redirect = redirectDefinitions.entries
            .firstOrNull { it.key == request.requestURI }
        if (authentication != null && authentication.isAuthenticated && redirect != null) {
            response.sendRedirect(request.contextPath + redirect.value)
        } else {
            filterChain.doFilter(request, response)
        }
    }
}
