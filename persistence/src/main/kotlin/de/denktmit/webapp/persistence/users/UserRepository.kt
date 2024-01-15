package de.denktmit.webapp.persistence.users

import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<UserEntity, Long> {

    fun findOneByMail(mail: String): UserEntity?

    fun existsByMail(mail: String): Boolean

}