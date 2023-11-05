package de.denktmit.webapp.springconfig

import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootConfiguration
@ComponentScan("de.denktmit.webapp.business")
@EnableJpaRepositories("de.denktmit.webapp.business.*")
@EnableConfigurationProperties(
    BusinessContextConfigProperties::class,
)
class BusinessContext