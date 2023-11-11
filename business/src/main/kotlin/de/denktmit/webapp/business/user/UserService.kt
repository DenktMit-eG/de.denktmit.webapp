package de.denktmit.webapp.business.user

import de.denktmit.webapp.common.DataTable
import de.denktmit.webapp.persistence.users.UserRepository
import org.springframework.stereotype.Service
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Service
class UserService(
    private val userRepository: UserRepository
): UserRepository by userRepository {

    private val f: DateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME

    fun userData(): DataTable {
        val meta = DataTable.Meta.create("ID", "E-Mail", "Disabled", "Locked Until", "Account Valid Until", "Credentials Valid Until")
        val rows = this.userRepository.findAll().map { entry ->
            meta.createRow(
                entry.id,
                entry.mail,
                entry.disabled,
                f.format(entry.lockedUntil.atZone(ZoneId.systemDefault())),
                f.format(entry.accountValidUntil.atZone(ZoneId.systemDefault())),
                f.format(entry.credentialsValidUntil.atZone(ZoneId.systemDefault())),
            )
        }
        return DataTable.create(meta, rows)
    }

}