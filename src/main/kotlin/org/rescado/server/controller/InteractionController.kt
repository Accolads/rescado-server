package org.rescado.server.controller

import org.rescado.server.controller.dto.build
import org.rescado.server.controller.dto.req.AddInteractionDTO
import org.rescado.server.controller.dto.res.InteractionDTO
import org.rescado.server.controller.dto.res.Response
import org.rescado.server.controller.dto.res.error.BadRequest
import org.rescado.server.controller.dto.res.error.Forbidden
import org.rescado.server.persistence.entity.Account
import org.rescado.server.service.AnimalService
import org.rescado.server.service.LikeService
import org.rescado.server.service.MessageService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/interaction")
class InteractionController(
    private val likeService: LikeService,
    private val animalService: AnimalService,
    private val messageService: MessageService,
) {

    @PostMapping("/like")
    fun addLikes(
        @Valid @RequestBody dto: AddInteractionDTO,
        res: BindingResult,
    ): ResponseEntity<Response> {
        val user = SecurityContextHolder.getContext().authentication.principal
        if (user !is Account)
            return Forbidden(error = messageService["error.LikeForbidden.message"]).build()

        if (res.hasErrors())
            return BadRequest(errors = res.allErrors.map { it.defaultMessage as String }).build()

        val successfullyLikedAnimalIds = mutableListOf<Long>()
        val alreadyLikedAnimalIds = mutableListOf<Long>()
        val nonExistentAnimalIds = mutableListOf<Long>()

        dto.ids.forEach { id ->
            animalService.getById(id)?.let {
                try {
                    likeService.create(user, it)
                    successfullyLikedAnimalIds.add(id)
                } catch (e: Exception) { // TODO only catch SQL Exception that is thrown when adding duplicates
                    alreadyLikedAnimalIds.add(id)
                }
            } ?: run {
                nonExistentAnimalIds.add(id)
            }
        }

        return InteractionDTO(
            likes = successfullyLikedAnimalIds,
            errors = alreadyLikedAnimalIds.map { messageService["error.AlreadyLikedAnimal.message", it] } +
                nonExistentAnimalIds.map { messageService["error.NonExistentAnimal.message", it] }
        ).build()
    }
}
