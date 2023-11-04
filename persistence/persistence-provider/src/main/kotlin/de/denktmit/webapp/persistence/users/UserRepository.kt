package de.denktmit.webapp.persistence.users

import org.springframework.data.repository.CrudRepository
import java.util.*

interface UserRepository: CrudRepository<User, UUID>