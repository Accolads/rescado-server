package org.rescado.server.controller.dto.validation

import org.rescado.server.persistence.entity.Animal
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import kotlin.reflect.KClass

@Target(AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [AnimalKindValidator::class])
annotation class AnimalKind(
    val message: String = "{validation.AnimalKind.message}",
    val groups: Array<KClass<out Any>> = [],
    val payload: Array<KClass<out Any>> = [],
)

class AnimalKindValidator : ConstraintValidator<AnimalKind, Any> {

    override fun initialize(constraintAnnotation: AnimalKind) {}

    override fun isValid(value: Any?, context: ConstraintValidatorContext?): Boolean =
        when (value) {
            is Set<*> -> value.all { kind -> isValid(kind.toString()) }
            is String -> isValid(value)
            else -> true
        }

    private fun isValid(value: String): Boolean = enumValues<Animal.Kind>().any { it.name.equals(value, true) }
}
