package de.denktmit.webapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@SpringBootApplication(scanBasePackages = ["de.denktmit.webapp.springconfig"])
class WebappApplication

fun main(args: Array<String>) {
	runApplication<WebappApplication>(*args)
}
