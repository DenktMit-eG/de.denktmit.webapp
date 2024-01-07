package de.denktmit.webapp.web.registration

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

class RegistrationFormModel(

    @NotBlank
    @Max(255)
    @Email
    val emailAddress: String,

    @NotBlank
    @Min(10)
    @Max(500)
    val password: CharArray,

    @NotBlank
    @Min(10)
    @Max(500)
    val passwordRepeated: CharArray
)