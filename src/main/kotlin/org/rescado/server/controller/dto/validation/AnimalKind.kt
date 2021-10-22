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

class AnimalKindValidator : ConstraintValidator<AnimalKind, String> {

    override fun initialize(constraintAnnotation: AnimalKind) {}

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        return if (value == null) true else enumValues<Animal.Kind>().any { it.name.equals(value, true) }
    }
}
