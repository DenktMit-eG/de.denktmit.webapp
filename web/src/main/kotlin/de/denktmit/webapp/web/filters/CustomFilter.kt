package de.denktmit.webapp.web.filters

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import org.springframework.web.filter.GenericFilterBean
import java.io.IOException


class CustomFilter : GenericFilterBean() {
    @Throws(IOException::class, ServletException::class)
    override fun doFilter(
        request: ServletRequest,
        response: ServletResponse,
        chain: FilterChain
    ) {
        chain.doFilter(request, response)
    }
}