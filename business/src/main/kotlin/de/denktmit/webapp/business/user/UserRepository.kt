package de.denktmit.webapp.business.user

import de.denktmit.webapp.persistence.users.User
import org.springframework.data.repository.CrudRepository
import java.util.*

interface UserRepository: CrudRepository<User, UUID> {

    fun findByMail(mail: String): User?

}