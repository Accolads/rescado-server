package org.rescado.server.controller

import org.rescado.server.controller.dto.build
import org.rescado.server.controller.dto.req.AddAnimalDTO
import org.rescado.server.controller.dto.req.AddAnimalPhotoDTO
import org.rescado.server.controller.dto.req.AddShelterDTO
import org.rescado.server.controller.dto.req.PatchAnimalDTO
import org.rescado.server.controller.dto.req.PatchShelterDTO
import org.rescado.server.controller.dto.res.Response
import org.rescado.server.controller.dto.res.error.BadRequest
import org.rescado.server.controller.dto.res.error.Forbidden
import org.rescado.server.controller.dto.res.error.NotFound
import org.rescado.server.controller.dto.toAnimalDTO
import org.rescado.server.controller.dto.toImageArrayDTO
import org.rescado.server.controller.dto.toImageDTO
import org.rescado.server.controller.dto.toShelterArrayDTO
import org.rescado.server.controller.dto.toShelterDTO
import org.rescado.server.persistence.entity.Account
import org.rescado.server.persistence.entity.Admin
import org.rescado.server.persistence.entity.Animal
import org.rescado.server.persistence.entity.Image
import org.rescado.server.service.AnimalService
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
import java.time.ZonedDateTime
import javax.validation.Valid

@RestController
@RequestMapping("/shelter")
class ShelterController(
    private val shelterService: ShelterService,
    private val animalService: AnimalService,
    private val imageService: ImageService,
    private val messageService: MessageService,
    private val pointGenerator: PointGenerator,
) {
    // region Shelter

    @GetMapping("/all")
    fun getAll(): ResponseEntity<*> {
        val user = SecurityContextHolder.getContext().authentication.principal
        if (user !is Admin)
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
        if (user !is Admin)
            return Forbidden(error = messageService["error.ShelterForbidden.message"]).build()

        if (res.hasErrors())
            return BadRequest(errors = res.allErrors.map { it.defaultMessage as String }).build()

        return shelterService.create(
            name = dto.name!!,
            email = dto.email!!,
            website = dto.website!!,
            newsfeed = dto.newsfeed,
            address = dto.address!!,
            postalCode = dto.postalCode!!,
            city = dto.city!!,
            country = dto.country!!,
            geometry = pointGenerator.make(dto.latitude, dto.longitude)!!,
            logo = imageService.create(Image.Type.LOGO, dto.logo!!),
            banner = dto.banner?.let { imageService.create(Image.Type.BANNER, dto.banner) },
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

        val shelter = if (user is Account) user.shelter!! else shelterService.getById(id)
            ?: return NotFound(error = messageService["error.ShelterNotFound.message"]).build()

        if (res.hasErrors())
            return BadRequest(errors = res.allErrors.map { it.defaultMessage as String }).build()

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
            logo = dto.logo?.let { imageService.create(Image.Type.LOGO, dto.logo) },
            banner = dto.banner?.let { imageService.create(Image.Type.BANNER, dto.banner) },
        ).toShelterDTO().build()
    }

    @DeleteMapping("/{id}")
    fun removeById(
        @PathVariable id: Long,
    ): ResponseEntity<Response> {
        val user = SecurityContextHolder.getContext().authentication.principal
        if (user is Account && user.shelter?.id != id)
            return Forbidden(error = messageService["error.ShelterForbidden.message"]).build()

        val shelter = if (user is Account) user.shelter!! else shelterService.getById(id)
            ?: return NotFound(error = messageService["error.ShelterNotFound.message"]).build()

        shelterService.delete(shelter)
        return Response(httpStatus = HttpStatus.NO_CONTENT).build()
    }

    // endregion
    // region Animal

    @GetMapping("/{id}/animal/all")
    fun getAllAnimals(
        @PathVariable id: Long,
    ): ResponseEntity<*> {
        val shelter = shelterService.getById(id)
            ?: return BadRequest(error = messageService["error.ShelterNotFound.message"]).build()

        val now = ZonedDateTime.now()
        return shelter.animals.map { it.toAnimalDTO(now) }.build()
    }

    @GetMapping("/{id}/animal/{animalId}")
    fun getAnimalById(
        @PathVariable id: Long,
        @PathVariable animalId: Long,
    ): ResponseEntity<Response> {
        val shelter = shelterService.getById(id)
            ?: return NotFound(error = messageService["error.ShelterNotFound.message"]).build()

        val animal = shelter.animals.find { it.id == animalId }
            ?: return NotFound(error = messageService["error.AnimalNotFound.message"]).build()

        val now = ZonedDateTime.now()
        return animal.toAnimalDTO(now).build()
    }

    @PostMapping("/{id}/animal")
    fun addAnimal(
        @PathVariable id: Long,
        @Valid @RequestBody dto: AddAnimalDTO,
        res: BindingResult,
    ): ResponseEntity<Response> {
        val user = SecurityContextHolder.getContext().authentication.principal
        if (user is Account && user.shelter?.id != id)
            return Forbidden(error = messageService["error.ShelterForbidden.message"]).build()

        val shelter = (if (user is Account) user.shelter else shelterService.getById(id))
            ?: return NotFound(error = messageService["error.ShelterNotFound.message"]).build()

        if (res.hasErrors())
            return BadRequest(errors = res.allErrors.map { it.defaultMessage as String }).build()

        val now = ZonedDateTime.now()
        return animalService.create(
            shelter = shelter,
            kind = Animal.Kind.valueOf(dto.kind!!),
            breed = dto.breed!!,
            name = dto.name!!,
            description = dto.description!!,
            sex = Animal.Sex.valueOf(dto.sex!!),
            birthday = ZonedDateTime.parse(dto.birthday),
            weight = dto.weight!!,
            vaccinated = dto.vaccinated!!,
            sterilized = dto.sterilized!!,
            photos = dto.photos!!.map { imageService.create(Image.Type.BANNER, it) }.toMutableSet()
        ).toAnimalDTO(now).build(HttpStatus.CREATED)
    }

    @PatchMapping("/{id}/animal/{animalId}")
    fun patchAnimalById(
        @PathVariable id: Long,
        @PathVariable animalId: Long,
        @Valid @RequestBody dto: PatchAnimalDTO,
        res: BindingResult,
    ): ResponseEntity<Response> {
        val user = SecurityContextHolder.getContext().authentication.principal
        if (user is Account && user.shelter?.id != id)
            return Forbidden(error = messageService["error.ShelterForbidden.message"]).build()

        val shelter = if (user is Account) user.shelter!! else shelterService.getById(id)
            ?: return NotFound(error = messageService["error.ShelterNotFound.message"]).build()

        val animal = shelter.animals.find { it.id == animalId }
            ?: return NotFound(error = messageService["error.AnimalNotFound.message"]).build()

        if (res.hasErrors())
            return BadRequest(errors = res.allErrors.map { it.defaultMessage as String }).build()

        val now = ZonedDateTime.now()
        return animalService.update(
            animal = animal,
            kind = dto.kind?.let { Animal.Kind.valueOf(it) },
            breed = dto.breed,
            name = dto.name,
            description = dto.description,
            sex = dto.sex?.let { Animal.Sex.valueOf(it) },
            birthday = ZonedDateTime.parse(dto.birthday),
            weight = dto.weight,
            vaccinated = dto.vaccinated,
            sterilized = dto.sterilized,
        ).toAnimalDTO(now).build()
    }

    @DeleteMapping("/{id}/animal/{animalId}")
    fun removeAnimalById(
        @PathVariable id: Long,
        @PathVariable animalId: Long,
    ): ResponseEntity<Response> {
        val user = SecurityContextHolder.getContext().authentication.principal
        if (user is Account && user.shelter?.id != id)
            return Forbidden(error = messageService["error.ShelterForbidden.message"]).build()

        val shelter = if (user is Account) user.shelter!! else shelterService.getById(id)
            ?: return NotFound(error = messageService["error.ShelterNotFound.message"]).build()

        val animal = shelter.animals.find { it.id == animalId }
            ?: return NotFound(error = messageService["error.AnimalNotFound.message"]).build()

        animalService.delete(animal)
        return Response(httpStatus = HttpStatus.NO_CONTENT).build()
    }

    // endregion
    // region Animal.photos

    @GetMapping("/{id}/animal/{animalId}/photo/all")
    fun getAllAnimalPhotos(
        @PathVariable id: Long,
        @PathVariable animalId: Long,
    ): ResponseEntity<*> {
        val shelter = shelterService.getById(id)
            ?: return BadRequest(error = messageService["error.ShelterNotFound.message"]).build()

        val animal = shelter.animals.find { it.id == animalId }
            ?: return NotFound(error = messageService["error.AnimalNotFound.message"]).build()

        return animal.photos.toImageArrayDTO().build()
    }

    @GetMapping("/{id}/animal/{animalId}/photo/{photoId}")
    fun getAnimalPhotoById(
        @PathVariable id: Long,
        @PathVariable animalId: Long,
        @PathVariable photoId: Long,
    ): ResponseEntity<Response> {
        val shelter = shelterService.getById(id)
            ?: return BadRequest(error = messageService["error.ShelterNotFound.message"]).build()

        val animal = shelter.animals.find { it.id == animalId }
            ?: return NotFound(error = messageService["error.AnimalNotFound.message"]).build()

        val photo = animal.photos.find { it.id == photoId }
            ?: return NotFound(error = messageService["error.AnimalPhotoNotFound.message"]).build()

        return photo.toImageDTO().build()
    }

    @PostMapping("/{id}/animal/{animalId}/photo")
    fun addAnimalPhoto(
        @PathVariable id: Long,
        @PathVariable animalId: Long,
        @Valid @RequestBody dto: AddAnimalPhotoDTO,
    ): ResponseEntity<*> {
        val user = SecurityContextHolder.getContext().authentication.principal
        if (user is Account && user.shelter?.id != id)
            return Forbidden(error = messageService["error.ShelterForbidden.message"]).build()

        val shelter = if (user is Account) user.shelter!! else shelterService.getById(id)
            ?: return NotFound(error = messageService["error.ShelterNotFound.message"]).build()

        val animal = shelter.animals.find { it.id == animalId }
            ?: return NotFound(error = messageService["error.AnimalNotFound.message"]).build()

        return animalService.addPhoto(animal, imageService.create(Image.Type.PHOTO, dto.reference!!))
            .photos.toImageArrayDTO().build()
    }

    @DeleteMapping("/{id}/animal/{animalId}/photo/{photoId}")
    fun removeAnimalPhotoById(
        @PathVariable id: Long,
        @PathVariable animalId: Long,
        @PathVariable photoId: Long,
    ): ResponseEntity<Response> {
        val user = SecurityContextHolder.getContext().authentication.principal
        if (user is Account && user.shelter?.id != id)
            return Forbidden(error = messageService["error.ShelterForbidden.message"]).build()

        val shelter = if (user is Account) user.shelter!! else shelterService.getById(id)
            ?: return NotFound(error = messageService["error.ShelterNotFound.message"]).build()

        val animal = shelter.animals.find { it.id == animalId }
            ?: return NotFound(error = messageService["error.AnimalNotFound.message"]).build()

        val photo = animal.photos.find { it.id == photoId }
            ?: return NotFound(error = messageService["error.AnimalPhotoNotFound.message"]).build()

        animalService.removePhoto(animal, photo)
        return Response(httpStatus = HttpStatus.NO_CONTENT).build()
    }

    // endregion
}
