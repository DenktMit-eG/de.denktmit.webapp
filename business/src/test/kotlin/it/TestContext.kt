package it

import de.denktmit.webapp.springconfig.BusinessContextConfigProperties
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.data.jpa.repository.config.EnableJpaRepositories


@EntityScan("de.denktmit.webapp.persistence.*")
@EnableJpaRepositories("de.denktmit.webapp.business.*")
@EnableAutoConfiguration
@EnableConfigurationProperties(BusinessContextConfigProperties::class)
class TestContext