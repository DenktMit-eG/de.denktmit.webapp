package de.denktmit.webapp.web.registration

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class PasswordMatchesValidator : ConstraintValidator<PasswordMatches, RegistrationFormData> {
    override fun initialize(constraintAnnotation: PasswordMatches) {
        // intentionally empty
    }

    override fun isValid(obj: RegistrationFormData, context: ConstraintValidatorContext): Boolean {
        val passwordMatches = obj.password.contentEquals(obj.passwordRepeated)
        if (!passwordMatches) {
            context.disableDefaultConstraintViolation()
            context.buildConstraintViolationWithTemplate("{PasswordNotMatching}")
                .addPropertyNode(RegistrationFormData::passwordRepeated.name)
                .addConstraintViolation()
        }
        return passwordMatches
    }
}
