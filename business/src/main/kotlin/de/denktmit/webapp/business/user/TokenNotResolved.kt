package de.denktmit.webapp.business.user

import java.time.Instant
import java.util.*

data class TokenNotResolved(val token: UUID, val action: String, val validationTime: Instant)