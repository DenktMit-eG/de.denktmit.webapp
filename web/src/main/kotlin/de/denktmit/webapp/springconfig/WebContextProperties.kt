package de.denktmit.webapp.springconfig

import jakarta.validation.constraints.*
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import java.net.URI
import java.util.*

@Configuration
@ConfigurationProperties(prefix = "web")
class WebContextProperties {

    var protocol: @NotNull HTTP_PROTOCOL? = HTTP_PROTOCOL.HTTP

    var hostname: @NotBlank @Pattern(regexp = "^[a-zA-Z]+$/") String =
        ""

    var port: @Min(1) @Max(65536) Int = 0

    val baseUri: URI?
        get() {
            val baseUri = if (port != 0) {
                protocol!!.toLowercaseString() + "://$hostname:$port"
            } else {
                protocol!!.toLowercaseString() + "://$hostname"
            }
            return URI.create(baseUri)
        }

    enum class HTTP_PROTOCOL(val defaultPort: Int) {
        HTTP(80), HTTPS(443);

        fun toLowercaseString(): String {
            return name.lowercase(Locale.getDefault())
        }
    }
}