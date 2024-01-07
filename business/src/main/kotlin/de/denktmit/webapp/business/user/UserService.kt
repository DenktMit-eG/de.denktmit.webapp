package de.denktmit.webapp.business.user

import de.denktmit.webapp.common.DataTable
import de.denktmit.webapp.persistence.users.UserRepository
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


@Service
class UserService(
    private val userRepository: UserRepository,
): UserRepository by userRepository {

    private val z: ZoneId = ZoneOffset.UTC
    private val f: DateTimeFormatter = DateTimeFormatter.RFC_1123_DATE_TIME

    fun userData(): DataTable {
        val meta = DataTable.Meta.create("E-Mail", "Disabled", "Locked Until", "Account Valid Until", "Credentials Valid Until")
        val allUsers = this.userRepository.findAll()
        val rows = allUsers.map { entry ->
            meta.createRow(
                entry.mail,
                entry.disabled,
                entry.lockedUntil.toDateTimeString(),
                entry.accountValidUntil.toDateTimeString(),
                entry.credentialsValidUntil.toDateTimeString(),
            )
        }
        return DataTable.create(meta, rows)
    }

    private fun Instant.toDateTimeString(zoneId: ZoneId = z, formatter: DateTimeFormatter = f): String {
        val zonedDateTime = ZonedDateTime.ofInstant(this, zoneId)
        return zonedDateTime.format(formatter)
    }


}