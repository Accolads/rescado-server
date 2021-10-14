package org.rescado.server.controller

import org.rescado.server.constant.SecurityConstants
import org.rescado.server.controller.dto.build
import org.rescado.server.controller.dto.req.AddShelterDTO
import org.rescado.server.controller.dto.req.PatchShelterDTO
import org.rescado.server.controller.dto.res.Response
import org.rescado.server.controller.dto.res.error.BadRequest
import org.rescado.server.controller.dto.res.error.Forbidden
import org.rescado.server.controller.dto.res.error.NotFound
import org.rescado.server.controller.dto.toShelterArrayDTO
import org.rescado.server.controller.dto.toShelterDTO
import org.rescado.server.persistence.entity.Account
import org.rescado.server.persistence.entity.Image
import org.rescado.server.service.ImageService
import org.rescado.server.service.MessageService
import org.rescado.server.service.ShelterService
import org.rescado.server.util.PointGenerator
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping(
    value = [
        "/shelter",
        "${SecurityConstants.ADMIN_ROUTE}/shelter"
    ]
)
class ShelterController(
    private val shelterService: ShelterService,
    private val imageService: ImageService,
    private val messageService: MessageService,
    private val pointGenerator: PointGenerator,
) {

    @GetMapping("/all")
    fun getAll(): ResponseEntity<*> {
        val user = SecurityContextHolder.getContext().authentication.principal
        if (user is Account)
            return Forbidden(error = messageService["error.ShelterForbidden.message"]).build()

        return shelterService.getAll().toShelterArrayDTO().build()
    }

    @GetMapping("/{id}")
    fun getById(
        @PathVariable id: Long,
    ): ResponseEntity<Response> {
        val shelter = shelterService.getById(id)
            ?: return NotFound(error = messageService["error.ShelterNotFound.message"]).build()

        return shelter.toShelterDTO().build()
    }

    @PostMapping
    fun add(
        @Valid @RequestBody dto: AddShelterDTO,
        res: BindingResult,
    ): ResponseEntity<Response> {
        val user = SecurityContextHolder.getContext().authentication.principal
        if (user is Account)
            return Forbidden(error = messageService["error.ShelterForbidden.message"]).build()

        if (res.hasErrors())
            return BadRequest(errors = res.allErrors.map { it.defaultMessage as String }).build()

        val logo = imageService.create(Image.Type.LOGO, dto.logo)
        val banner = dto.banner?.let { imageService.create(Image.Type.BANNER, dto.banner) }

        return shelterService.create(
            name = dto.name,
            email = dto.name,
            website = dto.website,
            newsfeed = dto.newsfeed,
            address = dto.address,
            postalCode = dto.postalCode,
            city = dto.city,
            country = dto.country,
            geometry = pointGenerator.make(dto.latitude, dto.longitude)!!,
            logo = logo,
            banner = banner
        ).toShelterDTO().build(HttpStatus.CREATED)
    }

    @PatchMapping("/{id}")
    fun patchById(
        @PathVariable id: Long,
        @Valid @RequestBody dto: PatchShelterDTO,
        res: BindingResult,
    ): ResponseEntity<Response> {
        val user = SecurityContextHolder.getContext().authentication.principal
        if (user is Account && user.shelter?.id != id)
            return Forbidden(error = messageService["error.ShelterForbidden.message"]).build()

        if (res.hasErrors())
            return BadRequest(errors = res.allErrors.map { it.defaultMessage as String }).build()

        val shelter = shelterService.getById(id)
            ?: return NotFound(error = messageService["error.ShelterNotFound.message"]).build()

        val logo = dto.logo?.let { imageService.create(Image.Type.LOGO, dto.logo) }
        val banner = dto.banner?.let { imageService.create(Image.Type.BANNER, dto.banner) }

        return shelterService.update(
            shelter = shelter,
            name = dto.name,
            email = dto.name,
            website = dto.website,
            newsfeed = dto.newsfeed,
            address = dto.address,
            postalCode = dto.postalCode,
            city = dto.city,
            country = dto.country,
            geometry = pointGenerator.make(dto.latitude, dto.longitude)!!,
            logo = logo,
            banner = banner,
        ).toShelterDTO().build()
    }

    @DeleteMapping("/{id}")
    fun removeById(
        @PathVariable id: Long,
    ): ResponseEntity<Response> {
        val user = SecurityContextHolder.getContext().authentication.principal
        if (user is Account && user.shelter?.id != id)
            return Forbidden(error = messageService["error.ShelterForbidden.message"]).build()

        val shelter = shelterService.getById(id)
            ?: return NotFound(error = messageService["error.ShelterNotFound.message"]).build()

        shelterService.delete(shelter)
        return Response(httpStatus = HttpStatus.NO_CONTENT).build()
    }
}
