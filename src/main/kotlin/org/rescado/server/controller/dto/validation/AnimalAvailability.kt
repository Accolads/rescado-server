package org.rescado.server.controller.dto.validation

import org.rescado.server.persistence.entity.Animal
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import kotlin.reflect.KClass

@Target(AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [AnimalAvailabilityValidator::class])
annotation class AnimalAvailability(
    val message: String = "{validation.AnimalAvailability.message}",
    val groups: Array<KClass<out Any>> = [],
    val payload: Array<KClass<out Any>> = [],
)

class AnimalAvailabilityValidator : ConstraintValidator<AnimalAvailability, String> {

    override fun initialize(constraintAnnotation: AnimalAvailability) {}

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        return if (value == null) true else enumValues<Animal.Availability>().any { it.name.equals(value, true) }
    }
}
