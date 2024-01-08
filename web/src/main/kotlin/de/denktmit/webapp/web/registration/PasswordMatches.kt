package de.denktmit.webapp.web.registration

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS, AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [PasswordMatchesValidator::class])
@MustBeDocumented
annotation class PasswordMatches(
    val message: String = "{PasswordNotMatching}",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)