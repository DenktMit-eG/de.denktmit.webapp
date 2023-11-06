package de.denktmit.webapp.persistence.users

import de.denktmit.webapp.persistence.users.User
import org.springframework.data.repository.CrudRepository
import java.util.*

interface UserRepository: CrudRepository<User, Long> {

    fun findByMail(mail: String): User?

}