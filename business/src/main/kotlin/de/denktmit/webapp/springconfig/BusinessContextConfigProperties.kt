package de.denktmit.webapp.springconfig

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "business")
class BusinessContextConfigProperties (
    val adminUsers: Set<String> = emptySet(),
    val domainNames: Set<String> = emptySet(),
    val mail: MailConfigProperties
)

class MailConfigProperties(
    val sender: String
)