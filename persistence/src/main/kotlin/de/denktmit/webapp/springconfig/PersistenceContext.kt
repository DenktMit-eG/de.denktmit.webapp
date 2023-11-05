package de.denktmit.webapp.springconfig

import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan

@SpringBootConfiguration
@EntityScan("de.denktmit.webapp.persistence.*")
class PersistenceContext