package de.denktmit.webapp.persistence.rbac

interface RbacRepository {

    fun findByMail(mail: String): RbacMapping?

}
