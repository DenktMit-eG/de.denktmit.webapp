package de.denktmit.webapp.persistence.otp

import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.time.Instant
import java.util.*

interface OtpRepository: CrudRepository<OtpAction, Long> {

    fun existsByTokenAndActionAndValidUntilAfter(token: UUID, action: String, instant: Instant): Boolean

    @Query("SELECT oa FROM OtpAction oa JOIN FETCH oa.user WHERE oa.token = :token AND oa.action = :action AND oa.validUntil > :validationTimestamp")
    fun findOneByTokenAndActionAndValidUntilAfter(
        token: UUID,
        action: String,
        validationTimestamp: Instant
    ): OtpAction?

    @Modifying
    @Query("DELETE FROM OtpAction a WHERE a.action = :action AND EXISTS (SELECT 1 FROM User u WHERE u.id = a.user.id AND u.mail = :mail)")
    fun deleteByUserMailAndAction(mail: String, action: String): Int


}