package de.denktmit.webapp.springconfig

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@ComponentScan("de.denktmit.webapp.business")
@Configuration
@EnableConfigurationProperties(
    BusinessContextConfigProperties::class,
)
class BusinessConfiguration