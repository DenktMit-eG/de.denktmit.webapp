package de.denktmit.webapp.web.user.internal

import de.denktmit.webapp.business.user.UserService
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext


class UniqueEmailValidator(private val userService: UserService) : ConstraintValidator<UniqueEmail, String> {

    override fun initialize(uniqueEmail: UniqueEmail) {
        // intentionally empty
    }

    override fun isValid(email: String?, context: ConstraintValidatorContext): Boolean {
        return email == null || !userService.isEmailExisting(email)
    }
}
