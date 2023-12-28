package de.denktmit.webapp.web.user.internal

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

const val UniqueEmailMessageCode = "de.denktmit.webapp.web.validators.UniqueEmailValidator.message"

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [UniqueEmailValidator::class])
annotation class UniqueEmail(
    val message: String = "{$UniqueEmailMessageCode}",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)