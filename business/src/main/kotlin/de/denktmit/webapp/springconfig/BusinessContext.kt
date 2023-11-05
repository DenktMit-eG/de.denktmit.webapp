package de.denktmit.webapp.springconfig

import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan

@SpringBootConfiguration
@ComponentScan("de.denktmit.webapp.business")
@EnableConfigurationProperties(
    BusinessContextConfigProperties::class,
)
class BusinessContext