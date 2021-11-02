package org.rescado.server.controller

import org.rescado.server.controller.dto.build
import org.rescado.server.controller.dto.req.GenerateCardsDTO
import org.rescado.server.controller.dto.res.Response
import org.rescado.server.controller.dto.res.error.BadRequest
import org.rescado.server.controller.dto.res.error.Forbidden
import org.rescado.server.controller.dto.toAnimalArrayDTO
import org.rescado.server.persistence.entity.Account
import org.rescado.server.persistence.entity.Animal
import org.rescado.server.service.MessageService
import org.rescado.server.service.SwipeService
import org.rescado.server.util.AreaData
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/cards")
class SwipeController(
    private val swipeService: SwipeService,
    private val messageService: MessageService,
) {

    @PostMapping("generate")
    fun generate(
        @Valid @RequestBody dto: GenerateCardsDTO?,
        res: BindingResult,
    ): ResponseEntity<*> {
        val user = SecurityContextHolder.getContext().authentication.principal
        if (user !is Account)
            return Forbidden(error = messageService["error.CardsForbidden.message"]).build()

        if (res.hasErrors())
            return BadRequest(errors = res.allErrors.map { it.defaultMessage as String }).build()

        val location = AreaData(dto?.latitude, dto?.longitude, dto?.radius)
        if (location.state == AreaData.State.INCOMPLETE)
            return BadRequest(error = messageService["error.PartialArea.message"]).build()

        if (dto?.minimumAge != null && dto.maximumAge != null && dto.minimumAge > dto.maximumAge)
            return BadRequest(error = messageService["error.InvalidAge.message"]).build()

        if (dto?.minimumWeight != null && dto.maximumWeight != null && dto.minimumWeight > dto.maximumWeight)
            return BadRequest(error = messageService["error.InvalidWeight.message"]).build()

        return swipeService.createCards(
            account = user,
            location = location,
            kinds = dto?.kinds?.map { Animal.Kind.valueOf(it.uppercase()) }?.toSet() ?: emptySet(),
            sexes = dto?.sexes?.map { Animal.Sex.valueOf(it.uppercase()) }?.toSet() ?: emptySet(),
            minimumAge = dto?.minimumAge,
            maximumAge = dto?.maximumAge,
            minimumWeight = dto?.minimumWeight,
            maximumWeight = dto?.maximumWeight,
            vaccinated = dto?.vaccinated,
            sterilized = dto?.sterilized,
        ).toAnimalArrayDTO().build()
    }

    @PostMapping("reset")
    fun reset(): ResponseEntity<Response> {
        val user = SecurityContextHolder.getContext().authentication.principal
        if (user !is Account)
            return Forbidden(error = messageService["error.CardsForbidden.message"]).build()

        swipeService.reset(user)
        return Response(httpStatus = HttpStatus.NO_CONTENT).build()
    }
}
