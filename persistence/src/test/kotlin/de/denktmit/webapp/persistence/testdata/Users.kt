package de.denktmit.webapp.persistence.testdata

import de.denktmit.webapp.persistence.Constants.FAR_FUTURE
import de.denktmit.webapp.persistence.Constants.FAR_PAST
import de.denktmit.webapp.persistence.users.User
import de.denktmit.webapp.persistence.users.UserRepository
import de.denktmit.webapp.persistence.users.UserRole
import java.time.Instant

object Users {

    fun createUser(
        userId: Long = 0,
        mail: String = "j.unit@denktmit.de",
        password: String = "very-secret",
        disabled: Boolean = false,
        lockedUntil: Instant = FAR_PAST,
        accountValidUntil: Instant = FAR_FUTURE,
        credentialsValidUntil: Instant = FAR_FUTURE,
        role: UserRole = UserRole.ADMIN
    ): User {
        return User(
            id = userId,
            mail = mail,
            password = password,
            disabled = disabled,
            lockedUntil = lockedUntil,
            accountValidUntil = accountValidUntil,
            credentialsValidUntil = credentialsValidUntil,
            role = role
        )
    }

    val johndoe = createUser(
        userId = -100,
        mail = "user1@example.com",
        password = "hashed_password_1",
        disabled = false,
        accountValidUntil = FAR_FUTURE,
        credentialsValidUntil = FAR_FUTURE,
        role = UserRole.USER
    )

    val janesmith = createUser(
        userId = -200,
        mail = "admin2@example.com",
        password = "hashed_password_2",
        disabled = false,
        accountValidUntil = FAR_FUTURE,
        credentialsValidUntil = FAR_FUTURE,
        role = UserRole.ADMIN
    )

    val alicejohnson = createUser(
        userId = -300,
        mail = "locked_user3@example.com",
        password = "hashed_password_3",
        disabled = true,
        lockedUntil = FAR_PAST,
        accountValidUntil = FAR_FUTURE,
        credentialsValidUntil = FAR_FUTURE,
        role = UserRole.USER
    )

    val paulhiggins = createUser(
        userId = -400,
        mail = "account_expired_user4@example.com",
        password = "hashed_password_4",
        disabled = true,
        accountValidUntil = FAR_PAST,
        credentialsValidUntil = FAR_FUTURE,
        role = UserRole.USER
    )

    val petergabriel = createUser(
        userId = -500,
        mail = "credentials_expired_user5@example.com",
        password = "hashed_password_5",
        disabled = true,
        accountValidUntil = FAR_FUTURE,
        credentialsValidUntil = FAR_PAST,
        role = UserRole.USER
    )

    val all = listOf(johndoe, janesmith, alicejohnson, paulhiggins, petergabriel)

    open class RepoStub(
        data: MutableList<User> = all.toMutableList()
    ): CrudRepositoryStub<User, Long>(data), UserRepository {

        override fun findByMail(mail: String): User? {
            return findAll().find { it.mail == mail }
        }
    }

}