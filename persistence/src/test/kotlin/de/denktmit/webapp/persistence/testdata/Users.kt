package de.denktmit.webapp.persistence.testdata

import de.denktmit.webapp.persistence.Constants.FAR_FUTURE
import de.denktmit.webapp.persistence.Constants.FAR_PAST
import de.denktmit.webapp.persistence.users.User
import de.denktmit.webapp.persistence.users.UserRepository
import java.time.Instant

object Users {

    fun createUser(
        userId: Long = 0,
        mail: String = "j.unit@denktmit.de",
        mailVerified: Boolean = true,
        password: String = "very-secret",
        disabled: Boolean = false,
        lockedUntil: Instant = FAR_PAST,
        accountValidUntil: Instant = FAR_FUTURE,
        credentialsValidUntil: Instant = FAR_FUTURE,
    ): User {
        return User(
            id = userId,
            mail = mail,
            mailVerified = mailVerified,
            password = password,
            disabled = disabled,
            lockedUntil = lockedUntil,
            accountValidUntil = accountValidUntil,
            credentialsValidUntil = credentialsValidUntil,
        )
    }

    val johndoe = createUser(
        userId = -100,
        mail = "user1.johndoe@example.com",
        mailVerified = true,
        password = "{noop}johndoe",
        disabled = false,
        accountValidUntil = FAR_FUTURE,
        credentialsValidUntil = FAR_FUTURE,
    )

    val janesmith = createUser(
        userId = -200,
        mail = "admin2.janesmith@example.com",
        mailVerified = true,
        password = "{noop}janesmith",
        disabled = false,
        accountValidUntil = FAR_FUTURE,
        credentialsValidUntil = FAR_FUTURE,
    )

    val alicejohnson = createUser(
        userId = -300,
        mail = "locked_user.alicejohnson@example.com",
        mailVerified = true,
        password = "{noop}alicejohnson",
        disabled = true,
        lockedUntil = FAR_FUTURE,
        accountValidUntil = FAR_FUTURE,
        credentialsValidUntil = FAR_FUTURE,
    )

    val paulhiggins = createUser(
        userId = -400,
        mail = "account_expired_user4.paulhiggins@example.com",
        mailVerified = false,
        password = "{noop}paulhiggins",
        disabled = true,
        accountValidUntil = FAR_PAST,
        credentialsValidUntil = FAR_FUTURE,
    )

    val petergabriel = createUser(
        userId = -500,
        mail = "creds_expired_user5.petergabriel@example.com",
        mailVerified = false,
        password = "{noop}petergabriel",
        disabled = false,
        accountValidUntil = FAR_FUTURE,
        credentialsValidUntil = FAR_PAST,
    )

    val all = listOf(johndoe, janesmith, alicejohnson, paulhiggins, petergabriel)

    open class RepoStub(
        val data: MutableList<User> = all.toMutableList()
    ): CrudRepositoryStub<User, Long>(data), UserRepository {

        override fun findOneByMail(mail: String): User? {
            return findAll().find { it.mail == mail }
        }

        override fun findAllByMailIn(mails: List<String>): List<User> {
            return findAll().toList()
        }

        override fun existsByMail(mail: String): Boolean {
            return findOneByMail(mail) != null
        }

    }

}