package it

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories


@EnableAutoConfiguration
@ComponentScan("de.denktmit.webapp.persistence.*")
@EnableJpaRepositories("de.denktmit.webapp.persistence.*")
@EntityScan("de.denktmit.webapp.persistence.*")
class PersistenceTestContext