package de.denktmit.webapp.web.user.internal

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

const val PasswordMatchesMessageCode = "de.denktmit.webapp.web.validators.PasswordMatchesValidator.message"

@Target(AnnotationTarget.CLASS, AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [PasswordMatchesValidator::class])
@MustBeDocumented
annotation class PasswordMatches(
    val message: String = "{$PasswordMatchesMessageCode}",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)