package de.denktmit.webapp.springconfig

import de.denktmit.webapp.webwicket.WebappUIApplication
import de.denktmit.wicket.spring.SpringContextUtil
import de.denktmit.wicket.spring.WicketFilterWrapper
import jakarta.servlet.DispatcherType
import jakarta.servlet.Filter
import org.apache.wicket.RuntimeConfigurationType
import org.apache.wicket.protocol.http.WicketFilter
import org.springframework.boot.web.server.ErrorPage
import org.springframework.boot.web.server.ErrorPageRegistrar
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import java.util.*

@Configuration
class WicketContext {
  @Bean
  fun getWicketApplication(env: Environment): WebappUIApplication {
    val isDev =
      env.activeProfiles
        .takeIf { it.isNotEmpty() }
        ?.any { it.contains("development") } ?: false

    return WebappUIApplication().apply {
      configurationType =
        if (isDev) {
          RuntimeConfigurationType.DEVELOPMENT
        } else {
          RuntimeConfigurationType.DEPLOYMENT
        }
    }
  }

  @Bean
  fun getWicketFilter(webApplication: WebappUIApplication): FilterRegistrationBean<out Filter> {
    val filter =
      WicketFilter(webApplication).apply {
        filterPath = "/"
      }

    return FilterRegistrationBean(WicketFilterWrapper(filter)).apply {
        order = -10
        setDispatcherTypes(EnumSet.allOf(DispatcherType::class.java))
    }
  }

  @Bean
  fun springContextUtil() = SpringContextUtil()

  @Bean
  fun errorPageRegistrar(): ErrorPageRegistrar =
    ErrorPageRegistrar {
      it.addErrorPages(
        ErrorPage(HttpStatus.NOT_FOUND, "/error/404"),
        ErrorPage(HttpStatus.FORBIDDEN, "/error/403"),
      )
    }
}
