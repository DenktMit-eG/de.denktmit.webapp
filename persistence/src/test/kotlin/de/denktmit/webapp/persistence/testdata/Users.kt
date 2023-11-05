package de.denktmit.webapp.persistence.testdata

import de.denktmit.webapp.persistence.Constants.FAR_FUTURE
import de.denktmit.webapp.persistence.otp.OtpAction
import de.denktmit.webapp.persistence.users.User
import de.denktmit.webapp.persistence.users.UserRole
import java.time.Instant
import java.util.*
import kotlin.reflect.KClass

object Users {

    fun createUser(
        userId: Long = 0,
        mail: String = "j.unit@denktmit.de",
        password: String = "very-secret",
        firstName: String = "jay",
        lastName: String = "unit",
        disabled: Boolean = false,
        lockedUntil: Instant? = null,
        accountValidUntil: Instant = FAR_FUTURE,
        credentialsValidUntil: Instant = FAR_FUTURE,
        role: UserRole = UserRole.ADMIN
    ): User {
        return User(
            userId = userId,
            mail = mail,
            password = password,
            firstName = firstName,
            lastName = lastName,
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
        firstName = "John",
        lastName = "Doe",
        disabled = false,
        accountValidUntil = Instant.parse("9999-12-31T23:59:59.999999Z"),
        credentialsValidUntil = Instant.parse("9999-12-31T23:59:59.999999Z"),
        role = UserRole.USER
    )

    val janesmith = createUser(
        userId = -200,
        mail = "admin2@example.com",
        password = "hashed_password_2",
        firstName = "Jane",
        lastName = "Smith",
        disabled = false,
        accountValidUntil = Instant.parse("9999-12-31T23:59:59.999999Z"),
        credentialsValidUntil = Instant.parse("9999-12-31T23:59:59.999999Z"),
        role = UserRole.ADMIN
    )

    val alicejohnson = createUser(
        userId = -300,
        mail = "locked_user3@example.com",
        password = "hashed_password_3",
        firstName = "Alice",
        lastName = "Johnson",
        disabled = true,
        lockedUntil = Instant.parse("1970-01-01T00:00:00Z"),
        accountValidUntil = Instant.parse("9999-12-31T23:59:59.999999Z"),
        credentialsValidUntil = Instant.parse("9999-12-31T23:59:59.999999Z"),
        role = UserRole.USER
    )

    val paulhiggins = createUser(
        userId = -400,
        mail = "account_expired_user4@example.com",
        password = "hashed_password_4",
        firstName = "Paul",
        lastName = "Higgins",
        disabled = true,
        accountValidUntil = Instant.parse("1970-01-01T00:00:00Z"),
        credentialsValidUntil = Instant.parse("9999-12-31T23:59:59.999999Z"),
        role = UserRole.USER
    )

    val petergabriel = createUser(
        userId = -500,
        mail = "credentials_expired_user5@example.com",
        password = "hashed_password_5",
        firstName = "Peter",
        lastName = "Gabriel",
        disabled = true,
        accountValidUntil = Instant.parse("9999-12-31T23:59:59.999999Z"),
        credentialsValidUntil = Instant.parse("1970-01-01T00:00:00Z"),
        role = UserRole.USER
    )

    val allUsers = listOf(johndoe, janesmith, alicejohnson, paulhiggins, petergabriel)

}