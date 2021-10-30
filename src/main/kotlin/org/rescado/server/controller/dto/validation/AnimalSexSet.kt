package org.rescado.server.controller.dto.validation

import org.rescado.server.persistence.entity.Animal
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import kotlin.reflect.KClass

@Target(AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [AnimalSexSetValidator::class])
annotation class AnimalSexSet(
    val message: String = "{validation.AnimalSexSet.message}",
    val groups: Array<KClass<out Any>> = [],
    val payload: Array<KClass<out Any>> = [],
)

class AnimalSexSetValidator : ConstraintValidator<AnimalSexSet, Set<String>> {

    override fun initialize(constraintAnnotation: AnimalSexSet) {}

    override fun isValid(value: Set<String>?, context: ConstraintValidatorContext?) =
        value?.all { sex ->
            enumValues<Animal.Sex>().any { it.name.equals(sex, true) }
        } ?: true
}
