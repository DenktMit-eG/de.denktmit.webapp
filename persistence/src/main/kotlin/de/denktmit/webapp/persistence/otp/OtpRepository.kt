package de.denktmit.webapp.persistence.otp

import de.denktmit.webapp.persistence.otp.OtpAction
import org.springframework.data.repository.CrudRepository
import java.util.*

interface OtpRepository: CrudRepository<OtpAction, UUID> {


}