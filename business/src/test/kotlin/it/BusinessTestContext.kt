package it

import de.denktmit.webapp.springconfig.BusinessContextConfigProperties
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.data.jpa.repository.config.EnableJpaRepositories


@EnableAutoConfiguration
@EnableConfigurationProperties(BusinessContextConfigProperties::class)
@EnableJpaRepositories("de.denktmit.webapp.persistence.*")
@EntityScan("de.denktmit.webapp.persistence.*")
class BusinessTestContext