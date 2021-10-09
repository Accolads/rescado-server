package org.rescado.server.service

import org.springframework.context.MessageSource
import org.springframework.context.NoSuchMessageException
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Component

@Component
class MessageService(private val messageSource: MessageSource) {

    operator fun get(id: String): String = get(id, null)

    operator fun get(id: String, vararg args: Any?): String {
        return try {
            messageSource.getMessage(id, args, LocaleContextHolder.getLocale())
        } catch (e: NoSuchMessageException) {
            id
        }
    }
}
