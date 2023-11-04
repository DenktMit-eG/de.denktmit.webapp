package de.denktmit.webapp.persistence.otp

import org.springframework.data.repository.CrudRepository
import java.util.*

interface OtpRepository: CrudRepository<OtpAction, UUID>