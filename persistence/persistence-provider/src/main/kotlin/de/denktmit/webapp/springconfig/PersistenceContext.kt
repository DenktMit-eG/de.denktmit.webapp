package de.denktmit.webapp.springconfig

import org.springframework.boot.SpringBootConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootConfiguration
@EnableJpaRepositories("de.denktmit.webapp.persistence.*")
@ComponentScan("de.denktmit.webapp.persistence.*")
class PersistenceContext