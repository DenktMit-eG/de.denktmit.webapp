package de.denktmit.webapp.web.user.internal

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@PasswordMatches
class PasswordUpdateFormData(

    @get:NotNull
    @get:Size(min = 10, max = 500)
    val oldPassword: CharArray = CharArray(0),

    @get:NotNull
    @get:Size(min = 10, max = 500)
    override val password: CharArray = CharArray(0),

    @get:NotNull
    @get:Size(min = 10, max = 500)
    override val passwordRepeated: CharArray = CharArray(0)
): FormWithPasswordRepeat