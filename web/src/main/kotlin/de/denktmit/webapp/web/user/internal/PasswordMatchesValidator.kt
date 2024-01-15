package de.denktmit.webapp.web.user.internal

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class PasswordMatchesValidator : ConstraintValidator<PasswordMatches, FormWithPasswordRepeat> {
    override fun initialize(constraintAnnotation: PasswordMatches) {
        // intentionally empty
    }

    override fun isValid(obj: FormWithPasswordRepeat, context: ConstraintValidatorContext): Boolean {
        val passwordMatches = obj.password.contentEquals(obj.passwordRepeated)
        if (!passwordMatches) {
            context.disableDefaultConstraintViolation()
            context.buildConstraintViolationWithTemplate("{$PasswordMatchesMessageCode}")
                .addPropertyNode(RegistrationFormData::passwordRepeated.name)
                .addConstraintViolation()
        }
        return passwordMatches
    }
}
