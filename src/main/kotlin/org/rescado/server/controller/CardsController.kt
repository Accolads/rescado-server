package org.rescado.server.controller

import org.rescado.server.controller.dto.build
import org.rescado.server.controller.dto.res.error.Forbidden
import org.rescado.server.controller.dto.toAnimalArrayDTO
import org.rescado.server.persistence.entity.Account
import org.rescado.server.service.AnimalService
import org.rescado.server.service.MessageService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/cards")
class CardsController(
    private val animalService: AnimalService,
    private val messageService: MessageService,
) {

    @GetMapping
    fun get(): ResponseEntity<*> {
        val user = SecurityContextHolder.getContext().authentication.principal
        if (user !is Account)
            return Forbidden(error = messageService["error.CardsForbidden.message"]).build()

        return animalService.getCards(user).toAnimalArrayDTO().build()
    }
}
