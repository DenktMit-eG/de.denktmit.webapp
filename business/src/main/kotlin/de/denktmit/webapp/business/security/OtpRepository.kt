package de.denktmit.webapp.business.security

import de.denktmit.webapp.persistence.otp.OtpAction
import org.springframework.data.repository.CrudRepository
import java.util.*

interface OtpRepository: CrudRepository<OtpAction, UUID> {


}