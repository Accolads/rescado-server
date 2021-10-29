package org.rescado.server.controller

import org.rescado.server.controller.dto.build
import org.rescado.server.controller.dto.res.error.BadRequest
import org.rescado.server.controller.dto.res.error.Forbidden
import org.rescado.server.controller.dto.toAnimalArrayDTO
import org.rescado.server.persistence.entity.Account
import org.rescado.server.persistence.entity.Animal
import org.rescado.server.service.CardsService
import org.rescado.server.service.MessageService
import org.rescado.server.util.AreaData
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/cards")
class CardsController(
    private val cardsService: CardsService,
    private val messageService: MessageService,
) {

    @GetMapping // TODO Not idempotent, so perhaps we should use @PostMapping?
    fun get(
        @RequestParam("kind", required = false) kindList: String?,
        @RequestParam("sex", required = false) sexList: String?,
        @RequestParam("minage", required = false) minimumAge: Int?,
        @RequestParam("maxage", required = false) maximumAge: Int?,
        @RequestParam("minweight", required = false) minimumWeight: Int?,
        @RequestParam("maxweight", required = false) maximumWeight: Int?,
        @RequestParam("vaccinated", required = false) vaccinated: Boolean?,
        @RequestParam("sterilized", required = false) sterilized: Boolean?,
        @RequestParam("latitude", required = false) latitude: Double?,
        @RequestParam("longitude", required = false) longitude: Double?,
        @RequestParam("radius", required = false) radius: Int?,
    ): ResponseEntity<*> {
        val user = SecurityContextHolder.getContext().authentication.principal
        if (user !is Account)
            return Forbidden(error = messageService["error.CardsForbidden.message"]).build()

        val location = AreaData(latitude, longitude, radius)
        if (location.state == AreaData.State.INCOMPLETE)
            return BadRequest(error = messageService["error.PartialArea.message"]).build()

        val kinds: List<Animal.Kind>
        try {
            kinds = if (!kindList.isNullOrBlank()) kindList.split(",").map { Animal.Kind.valueOf(it.uppercase()) } else emptyList()
        } catch (e: IllegalArgumentException) {
            return BadRequest(error = messageService["error.InvalidKind.message"]).build()
        }

        val sexes: List<Animal.Sex>
        try {
            sexes = if (!sexList.isNullOrBlank()) sexList.split(",").map { Animal.Sex.valueOf(it.uppercase()) } else emptyList()
        } catch (e: IllegalArgumentException) {
            return BadRequest(error = messageService["error.InvalidSex.message"]).build()
        }

        if (minimumAge != null && maximumAge != null && minimumAge > maximumAge)
            return BadRequest(error = messageService["error.InvalidAge.message"]).build()

        if (minimumWeight != null && maximumWeight != null && minimumWeight > maximumWeight)
            return BadRequest(error = messageService["error.InvalidWeight.message"]).build()

        return cardsService.getCards(
            account = user,
            location = location,
            kinds = kinds,
            sexes = sexes,
            minimumAge = minimumAge,
            maximumAge = maximumAge,
            minimumWeight = minimumWeight,
            maximumWeight = maximumWeight,
            vaccinated = vaccinated,
            sterilized = sterilized,
        ).toAnimalArrayDTO().build()
    }
}
