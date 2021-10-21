package org.rescado.server.controller.dto.validation

import org.rescado.server.persistence.entity.Animal
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import kotlin.reflect.KClass

@Target(AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [AnimalSexValidator::class])
annotation class AnimalSex(
    val message: String = "{validation.AnimalSex.message}",
    val groups: Array<KClass<out Any>> = [],
    val payload: Array<KClass<out Any>> = [],
)

class AnimalSexValidator : ConstraintValidator<AnimalSex, String> {

    override fun initialize(constraintAnnotation: AnimalSex) {}

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        return if (value == null) true else enumValues<Animal.Sex>().any { it.name.equals(value, true) }
    }
}
