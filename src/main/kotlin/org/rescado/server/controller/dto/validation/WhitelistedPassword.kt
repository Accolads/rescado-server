package org.rescado.server.controller.dto.validation

import org.rescado.server.constant.SecurityConstants
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import kotlin.reflect.KClass

@Target(AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [WhitelistedPasswordValidator::class])
annotation class WhitelistedPassword(
    val message: String = "{validation.WhitelistedPassword.message}",
    val groups: Array<KClass<out Any>> = [],
    val payload: Array<KClass<out Any>> = [],
)

class WhitelistedPasswordValidator : ConstraintValidator<WhitelistedPassword, String> {

    override fun initialize(constraintAnnotation: WhitelistedPassword) {}

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        SecurityConstants.PASSWORD_BLACKLIST.find { it.equals(value, ignoreCase = true) }?.let {
            return false
        }
        return true
    }
}
