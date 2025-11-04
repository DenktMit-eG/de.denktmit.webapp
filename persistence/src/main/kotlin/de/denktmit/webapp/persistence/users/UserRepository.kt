package de.denktmit.webapp.persistence.users

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<User, Long> {

    fun findOneByMail(mail: String): User?

    @Query("select u from User u where u.mail in (:mails)")
    fun findAllByMails(mails: List<String>): List<User>

    fun existsByMail(mail: String): Boolean

}