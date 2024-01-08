package de.denktmit.webapp.web.registration

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@PasswordMatches
class RegistrationFormData(

    @get:NotBlank
    @get:Size(min = 5, max = 255)
    @get:Email
    val emailAddress: String = "",

    @get:NotNull
    @get:Size(min = 10, max = 500)
    val password: CharArray = CharArray(0),

    @get:NotNull
    @get:Size(min = 10, max = 500)
    val passwordRepeated: CharArray = CharArray(0)
)