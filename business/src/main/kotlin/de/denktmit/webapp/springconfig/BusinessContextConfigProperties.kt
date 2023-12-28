package de.denktmit.webapp.springconfig

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties(prefix = "business")
class BusinessContextConfigProperties (
    val mail: MailConfigProperties,
    val adminUsers: Set<String> = emptySet(),
    val domainNames: Set<String> = emptySet(),
    val acceptInviteDuration: Duration = Duration.ofDays(10),
    val resetPasswordDuration: Duration = Duration.ofMinutes(5),
    val verifyMailDuration: Duration = Duration.ofDays(3),
)

class MailConfigProperties(
    val sender: String
)