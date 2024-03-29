package de.denktmit.webapp.persistence.users

interface RbacRepository {

    fun findOneByMail(mail: String): RbacMapping?

    fun findAllByMails(mails: Set<String>): List<RbacMapping>

    fun setUserGroups(mail: String, groupNames: Set<String>)

}
