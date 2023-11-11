package de.denktmit.webapp.springconfig

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@ComponentScan("de.denktmit.webapp.persistence.*")
@Configuration
@EnableJpaRepositories("de.denktmit.webapp.persistence.*")
@EntityScan("de.denktmit.webapp.persistence.*")
class PersistenceConfiguration