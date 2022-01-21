package org.rescado.server.controller

import org.rescado.server.controller.dto.build
import org.rescado.server.controller.dto.req.GenerateCardsDTO
import org.rescado.server.controller.dto.req.SetOfIdsDTO
import org.rescado.server.controller.dto.res.CardActionDTO
import org.rescado.server.controller.dto.res.LikeDTO
import org.rescado.server.controller.dto.res.Response
import org.rescado.server.controller.dto.res.error.BadRequest
import org.rescado.server.controller.dto.res.error.Forbidden
import org.rescado.server.controller.dto.toAnimalArrayDTO
import org.rescado.server.controller.dto.toAnimalDTO
import org.rescado.server.persistence.entity.Account
import org.rescado.server.persistence.entity.Animal
import org.rescado.server.service.AnimalService
import org.rescado.server.service.CardService
import org.rescado.server.service.MessageService
import org.rescado.server.util.AreaData
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/cards")
class CardController(
    private val cardService: CardService,
    private val animalService: AnimalService,
    private val messageService: MessageService,
) {
    // region generation

    @PostMapping("generate")
    fun generate(
        @Valid @RequestBody dto: GenerateCardsDTO,
        res: BindingResult,
    ): ResponseEntity<*> {
        val user = SecurityContextHolder.getContext().authentication.principal
        if (user !is Account)
            return Forbidden(error = messageService["error.ResourceForbidden.message"]).build()

        if (res.hasErrors())
            return BadRequest(errors = res.allErrors.map { it.defaultMessage as String }).build()

        val location = AreaData(dto.latitude, dto.longitude, dto.radius)
        if (location.state == AreaData.State.INCOMPLETE)
            return BadRequest(error = messageService["error.PartialArea.message"]).build()

        if (dto.minimumAge != null && dto.maximumAge != null && dto.minimumAge > dto.maximumAge)
            return BadRequest(error = messageService["error.InvalidAge.message"]).build()

        if (dto.minimumWeight != null && dto.maximumWeight != null && dto.minimumWeight > dto.maximumWeight)
            return BadRequest(error = messageService["error.InvalidWeight.message"]).build()

        return cardService.createCards(
            account = user,
            number = dto.number!!,
            location = location,
            kinds = dto.kinds?.map { Animal.Kind.valueOf(it.uppercase()) }?.toSet() ?: emptySet(),
            sexes = dto.sexes?.map { Animal.Sex.valueOf(it.uppercase()) }?.toSet() ?: emptySet(),
            minimumAge = dto.minimumAge,
            maximumAge = dto.maximumAge,
            minimumWeight = dto.minimumWeight,
            maximumWeight = dto.maximumWeight,
            vaccinated = dto.vaccinated,
            sterilized = dto.sterilized,
        ).toAnimalArrayDTO().build()
    }

    // endregion
    // region likes

    @GetMapping("liked")
    fun getLiked(
        @RequestParam detailed: Boolean = false,
    ): ResponseEntity<*> {
        val user = SecurityContextHolder.getContext().authentication.principal
        if (user !is Account)
            return Forbidden(error = messageService["error.ResourceForbidden.message"]).build()

        if (detailed)
            return cardService.getLikedByAccountWithAnimals(user).map {
                LikeDTO(
                    timestamp = it.timestamp,
                    reference = it.reference,
                    animal = it.animal.toAnimalDTO()
                )
            }.build()
        return ResponseEntity(cardService.getLikedByAccountWithAnimals(user).map { it.animal.id }, HttpStatus.OK)
    }

    @PostMapping("liked")
    fun addLiked(
        @Valid @RequestBody dto: SetOfIdsDTO,
        res: BindingResult,
    ): ResponseEntity<Response> {
        val user = SecurityContextHolder.getContext().authentication.principal
        if (user !is Account)
            return Forbidden(error = messageService["error.ResourceForbidden.message"]).build()

        if (res.hasErrors())
            return BadRequest(errors = res.allErrors.map { it.defaultMessage as String }).build()

        val nonExistentAnimalIds = mutableListOf<Long>()
        val alreadyLikedAnimalIds = mutableListOf<Long>()
        val successfullyLikedAnimalIds = mutableListOf<Long>()

        dto.ids!!.forEach { id ->
            animalService.getById(id)?.let {
                if (!cardService.checkIfLikeExists(user, it)) {
                    cardService.createLike(user, it)
                    successfullyLikedAnimalIds.add(id)
                } else {
                    alreadyLikedAnimalIds.add(id)
                }
            } ?: run {
                nonExistentAnimalIds.add(id)
            }
        }

        return CardActionDTO(
            liked = successfullyLikedAnimalIds,
            errors = nonExistentAnimalIds.map { messageService["error.NonExistentAnimal.message", it] } +
                alreadyLikedAnimalIds.map { messageService["error.AlreadyLikedAnimal.message", it] }
        ).build()
    }

    @DeleteMapping("liked")
    fun deleteLiked(
        @Valid @RequestBody dto: SetOfIdsDTO,
        res: BindingResult,
    ): ResponseEntity<Response> {
        val user = SecurityContextHolder.getContext().authentication.principal
        if (user !is Account)
            return Forbidden(error = messageService["error.ResourceForbidden.message"]).build()

        if (res.hasErrors())
            return BadRequest(errors = res.allErrors.map { it.defaultMessage as String }).build()

        val nonExistentAnimalIds = mutableListOf<Long>()
        val notLikedAnimalIds = mutableListOf<Long>()
        val successfullyLikedAnimalIds = mutableListOf<Long>()

        dto.ids!!.forEach { id ->
            animalService.getById(id)?.let {
                if (cardService.checkIfLikeExists(user, it)) {
                    cardService.deleteLike(user, it)
                    successfullyLikedAnimalIds.add(id)
                } else {
                    notLikedAnimalIds.add(id)
                }
            } ?: run {
                nonExistentAnimalIds.add(id)
            }
        }

        return CardActionDTO(
            liked = successfullyLikedAnimalIds,
            errors = nonExistentAnimalIds.map { messageService["error.NonExistentAnimal.message", it] } +
                notLikedAnimalIds.map { messageService["error.NotLikedAnimal.message", it] }
        ).build()
    }

    // endregion
    // region skipped

    @PostMapping("skipped")
    fun addSkipped(
        @Valid @RequestBody dto: SetOfIdsDTO,
        res: BindingResult,
    ): ResponseEntity<Response> {
        val user = SecurityContextHolder.getContext().authentication.principal
        if (user !is Account)
            return Forbidden(error = messageService["error.ResourceForbidden.message"]).build()

        if (res.hasErrors())
            return BadRequest(errors = res.allErrors.map { it.defaultMessage as String }).build()

        val nonExistentAnimalIds = mutableListOf<Long>()
        val alreadySkippedAnimalIds = mutableListOf<Long>()
        val successfullySkippedAnimalIds = mutableListOf<Long>()

        dto.ids!!.forEach { id ->
            animalService.getById(id)?.let {
                if (!cardService.checkIfSwipeExists(user, it)) {
                    cardService.createSwipe(user, it)
                    successfullySkippedAnimalIds.add(id)
                } else {
                    alreadySkippedAnimalIds.add(id)
                }
            } ?: run {
                nonExistentAnimalIds.add(id)
            }
        }

        return CardActionDTO(
            skipped = successfullySkippedAnimalIds,
            errors = nonExistentAnimalIds.map { messageService["error.NonExistentAnimal.message", it] } +
                alreadySkippedAnimalIds.map { messageService["error.AlreadySkippedAnimal.message", it] }
        ).build()
    }

    @DeleteMapping("skipped")
    fun reset(): ResponseEntity<Response> {
        val user = SecurityContextHolder.getContext().authentication.principal
        if (user !is Account)
            return Forbidden(error = messageService["error.ResourceForbidden.message"]).build()

        cardService.deleteSwiped(user)
        return Response(httpStatus = HttpStatus.NO_CONTENT).build()
    }

    // endregion
}
