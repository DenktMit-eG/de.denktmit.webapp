package de.denktmit.webapp

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication(
    exclude =
        [
            SecurityAutoConfiguration::class,
        ],
)
@ComponentScan(
    "de.denktmit.webapp.springconfig",
)
class WebwicketSpringApplication

fun main(args: Array<String>) {
    val app = SpringApplication(WebwicketSpringApplication::class.java)
    app.run(*args).environment
}
