package de.denktmit.webapp.springconfig

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("webapp.security")
class WebappSecurityProperties(
  val method: AuthMethod = AuthMethod.FORM_AUTH,
  val csrfSecure: Boolean = true,
  val anonymousPrincipal: String = "anonymous",
)
