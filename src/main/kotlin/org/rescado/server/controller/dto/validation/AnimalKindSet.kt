package org.rescado.server.controller.dto.validation

import org.rescado.server.persistence.entity.Animal
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import kotlin.reflect.KClass

@Target(AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [AnimalKindSetValidator::class])
annotation class AnimalKindSet(
    val message: String = "{validation.AnimalKindSet.message}",
    val groups: Array<KClass<out Any>> = [],
    val payload: Array<KClass<out Any>> = [],
)

class AnimalKindSetValidator : ConstraintValidator<AnimalKindSet, Set<String>> {

    override fun initialize(constraintAnnotation: AnimalKindSet) {}

    override fun isValid(value: Set<String>?, context: ConstraintValidatorContext?) =
        value?.all { kind ->
            enumValues<Animal.Kind>().any { it.name.equals(kind, true) }
        } ?: true
}
