package org.rescado.server.config

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.validation.Validator
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.util.Locale

@Configuration
class WebConfig : WebMvcConfigurer {

    @Bean
    fun messageSource(): MessageSource {
        Locale.setDefault(Locale.ENGLISH)
        val source = ReloadableResourceBundleMessageSource()
        source.setBasenames("classpath:messages")
        source.setDefaultEncoding("UTF-8")
        source.setUseCodeAsDefaultMessage(true)
        return source
    }

    @Bean
    override fun getValidator(): Validator {
        val validator = LocalValidatorFactoryBean()
        validator.setValidationMessageSource(this.messageSource())
        return validator
    }
}
