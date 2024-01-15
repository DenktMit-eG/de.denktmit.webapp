package de.denktmit.webapp.persistence.users

import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<User, Long> {

    fun findOneByMail(mail: String): User?

    fun existsByMail(mail: String): Boolean

}