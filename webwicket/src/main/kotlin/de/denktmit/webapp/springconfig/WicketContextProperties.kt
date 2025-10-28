package de.denktmit.webapp.springconfig

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import org.springframework.boot.context.properties.ConfigurationProperties
import java.net.URI

@ConfigurationProperties(prefix = "webwicket")
class WicketContextProperties(
    val protocol: HttpProtocol = HttpProtocol.HTTP,

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z]+$/")
    val hostname: String,

    @Min(1)
    @Max(65536)
    val port: Int = 0
) {

    val baseUri: URI
        get() {
            val baseUri = if (port != 0 && port != protocol.defaultPort) {
                protocol.name.lowercase() + "://$hostname:$port"
            } else {
                protocol.name.lowercase() + "://$hostname"
            }
            return URI.create(baseUri)
        }

    enum class HttpProtocol(val defaultPort: Int) {
        HTTP(80),
        HTTPS(443);
    }
}