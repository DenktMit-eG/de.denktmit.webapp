package de.denktmit.webapp.business.user

import de.denktmit.webapp.common.DataTable
import org.springframework.stereotype.Service
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Service
class UserService(
    private val userRepository: UserRepository
): UserRepository by userRepository {

    private val f: DateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME

    fun userData(): DataTable {
        val meta = DataTable.Meta.create("ID", "E-Mail", "Disabled", "Locked Until", "Account Valid Until", "Credentials Valid Until", "Role")
        val rows = this.userRepository.findAll().map { entry ->
            val lockedUntil =
                if (entry.lockedUntil != null ) f.format(entry.lockedUntil!!.atZone(ZoneId.systemDefault()))
                else ""
            meta.createRow(
                entry.userId,
                entry.mail,
                entry.disabled,
                lockedUntil,
                f.format(entry.accountValidUntil.atZone(ZoneId.systemDefault())),
                f.format(entry.credentialsValidUntil.atZone(ZoneId.systemDefault())),
                entry.role
            )
        }
        return DataTable.create(meta, rows)
    }

}