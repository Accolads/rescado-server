package org.rescado.server.controller

import org.rescado.server.constant.SecurityConstants
import org.rescado.server.controller.dto.build
import org.rescado.server.controller.dto.req.AddAnimalDTO
import org.rescado.server.controller.dto.req.AddAnimalPhotoDTO
import org.rescado.server.controller.dto.req.AddShelterDTO
import org.rescado.server.controller.dto.req.PatchAnimalDTO
import org.rescado.server.controller.dto.req.PatchShelterDTO
import org.rescado.server.controller.dto.res.Response
import org.rescado.server.controller.dto.res.error.BadRequest
import org.rescado.server.controller.dto.res.error.Conflict
import org.rescado.server.controller.dto.res.error.Forbidden
import org.rescado.server.controller.dto.res.error.NotFound
import org.rescado.server.controller.dto.toAnimalDTO
import org.rescado.server.controller.dto.toImageArrayDTO
import org.rescado.server.controller.dto.toImageDTO
import org.rescado.server.controller.dto.toShelterArrayDTO
import org.rescado.server.controller.dto.toShelterDTO
import org.rescado.server.controller.dto.withLinks
import org.rescado.server.persistence.entity.Account
import org.rescado.server.persistence.entity.Admin
import org.rescado.server.persistence.entity.Animal
import org.rescado.server.persistence.entity.Image
import org.rescado.server.persistence.entity.Shelter
import org.rescado.server.service.AccountService
import org.rescado.server.service.AnimalService
import org.rescado.server.service.ImageService
import org.rescado.server.service.MessageService
import org.rescado.server.service.ShelterService
import org.rescado.server.util.PointGenerator
import org.springframework.data.domain.Page
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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.lang.IllegalArgumentException
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@RestController
@RequestMapping("/shelter")
class ShelterController(
    private val shelterService: ShelterService,
    private val animalService: AnimalService,
    private val accountService: AccountService,
    private val imageService: ImageService,
    private val messageService: MessageService,
    private val pointGenerator: PointGenerator,
) {
    // region Shelter

    @GetMapping
    fun getPaged(
        @RequestParam("page", defaultValue = "0") page: Int,
        @RequestParam("limit", defaultValue = SecurityConstants.DEFAULT_LIMIT.toString()) size: Int,
        @RequestParam("sort") sort: String?,
        req: HttpServletRequest,
    ): ResponseEntity<*> {
        if (size > SecurityConstants.MAX_LIMIT)
            return BadRequest(error = messageService["error.LimitExceeded.message"]).build()
        try {
            val shelterPage: Page<Shelter> = shelterService.getPage(page, size, sort)
            if (page > shelterPage.totalPages - 1)
                return BadRequest(error = messageService["error.OutOfPages.message"]).build()

            return shelterPage.content.toShelterArrayDTO()
                .withLinks(req, shelterPage)
                .build()
        } catch (e: IllegalArgumentException) {
            return BadRequest(error = messageService["error.IllegalPagination.message"]).build()
        }
    }

    @GetMapping("/all")
    fun getAll(): ResponseEntity<*> {
        val user = SecurityContextHolder.getContext().authentication.principal
        if (user !is Admin)
            return Forbidden(error = messageService["error.ShelterForbidden.message"]).build()

        return shelterService.getAll().toShelterArrayDTO().build()
    }

    @GetMapping("/{shelterId}")
    fun getById(
        @PathVariable shelterId: Long,
    ): ResponseEntity<Response> {
        val shelter = shelterService.getById(shelterId)
            ?: return NotFound(error = messageService["error.NonExistentShelter.message", shelterId]).build()

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

    @PatchMapping("/{shelterId}")
    fun patchById(
        @PathVariable shelterId: Long,
        @Valid @RequestBody dto: PatchShelterDTO,
        res: BindingResult,
    ): ResponseEntity<Response> {
        val user = SecurityContextHolder.getContext().authentication.principal
        if (user is Account && user.shelter?.id != shelterId)
            return Forbidden(error = messageService["error.ShelterForbidden.message"]).build()

        val shelter = if (user is Account) user.shelter!! else shelterService.getById(shelterId)
            ?: return NotFound(error = messageService["error.NonExistentShelter.message", shelterId]).build()

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

    @DeleteMapping("/{shelterId}")
    fun removeById(
        @PathVariable shelterId: Long,
    ): ResponseEntity<Response> {
        val user = SecurityContextHolder.getContext().authentication.principal
        if (user !is Admin)
            return Forbidden(error = messageService["error.ShelterForbidden.message"]).build()

        val shelter = shelterService.getById(shelterId)
            ?: return NotFound(error = messageService["error.NonExistentShelter.message", shelterId]).build()

        if (accountService.getAllVolunteers(shelter).isNotEmpty())
            return Conflict(error = messageService["error.ShelterHasVolunteers.message"]).build()

        shelterService.delete(shelter)
        return Response(httpStatus = HttpStatus.NO_CONTENT).build()
    }

    // endregion
    // region Animal

    @GetMapping("/{shelterId}/animal/all")
    fun getAllAnimals(
        @PathVariable shelterId: Long,
    ): ResponseEntity<*> {
        val shelter = shelterService.getByIdWithAnimals(shelterId)
            ?: return NotFound(error = messageService["error.NonExistentShelter.message", shelterId]).build()

        val now = ZonedDateTime.now()
        return shelter.animals.map { it.toAnimalDTO(now) }.build()
    }

    @GetMapping("/{shelterId}/animal/{animalId}")
    fun getAnimalById(
        @PathVariable shelterId: Long,
        @PathVariable animalId: Long,
    ): ResponseEntity<Response> {
        val shelter = shelterService.getByIdWithAnimals(shelterId)
            ?: return NotFound(error = messageService["error.NonExistentShelter.message", shelterId]).build()

        val animal = shelter.animals.find { it.id == animalId }
            ?: return NotFound(error = messageService["error.NonExistentAnimal.message", animalId]).build()

        val now = ZonedDateTime.now()
        return animal.toAnimalDTO(now).build()
    }

    @PostMapping("/{shelterId}/animal")
    fun addAnimal(
        @PathVariable shelterId: Long,
        @Valid @RequestBody dto: AddAnimalDTO,
        res: BindingResult,
    ): ResponseEntity<Response> {
        val user = SecurityContextHolder.getContext().authentication.principal
        if (user is Account && user.shelter?.id != shelterId)
            return Forbidden(error = messageService["error.ShelterForbidden.message"]).build()

        val shelter = (if (user is Account) user.shelter else shelterService.getById(shelterId))
            ?: return NotFound(error = messageService["error.NonExistentShelter.message", shelterId]).build()

        if (res.hasErrors())
            return BadRequest(errors = res.allErrors.map { it.defaultMessage as String }).build()

        val now = ZonedDateTime.now()
        return animalService.create(
            shelter = shelter,
            kind = Animal.Kind.valueOf(dto.kind!!.uppercase()),
            breed = dto.breed!!,
            name = dto.name!!,
            description = dto.description!!,
            sex = Animal.Sex.valueOf(dto.sex!!.uppercase()),
            birthday = LocalDate.parse(dto.birthday, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
            weight = dto.weight!!,
            vaccinated = dto.vaccinated!!,
            sterilized = dto.sterilized!!,
            availability = Animal.Availability.valueOf(dto.availability!!.uppercase()),
            photos = dto.photos!!.map { imageService.create(Image.Type.PHOTO, it) }.toMutableSet()
        ).toAnimalDTO(now).build(HttpStatus.CREATED)
    }

    @PatchMapping("/{shelterId}/animal/{animalId}")
    fun patchAnimalById(
        @PathVariable shelterId: Long,
        @PathVariable animalId: Long,
        @Valid @RequestBody dto: PatchAnimalDTO,
        res: BindingResult,
    ): ResponseEntity<Response> {
        val user = SecurityContextHolder.getContext().authentication.principal
        if (user is Account && user.shelter?.id != shelterId)
            return Forbidden(error = messageService["error.ShelterForbidden.message"]).build()

        val shelter = shelterService.getByIdWithAnimals(shelterId)
            ?: return NotFound(error = messageService["error.NonExistentShelter.message", shelterId]).build()

        val animal = shelter.animals.find { it.id == animalId }
            ?: return NotFound(error = messageService["error.NonExistentAnimal.message", animalId]).build()

        if (res.hasErrors())
            return BadRequest(errors = res.allErrors.map { it.defaultMessage as String }).build()

        val now = ZonedDateTime.now()
        return animalService.update(
            animal = animal,
            kind = dto.kind?.let { Animal.Kind.valueOf(it.uppercase()) },
            breed = dto.breed,
            name = dto.name,
            description = dto.description,
            sex = dto.sex?.let { Animal.Sex.valueOf(it.uppercase()) },
            birthday = dto.birthday?.let { LocalDate.parse(dto.birthday, DateTimeFormatter.ofPattern("yyyy-MM-dd")) },
            weight = dto.weight,
            vaccinated = dto.vaccinated,
            sterilized = dto.sterilized,
            availability = dto.availability?.let { Animal.Availability.valueOf(it.uppercase()) },
        ).toAnimalDTO(now).build()
    }

    @DeleteMapping("/{shelterId}/animal/{animalId}")
    fun removeAnimalById(
        @PathVariable shelterId: Long,
        @PathVariable animalId: Long,
    ): ResponseEntity<Response> {
        val user = SecurityContextHolder.getContext().authentication.principal
        if (user is Account && user.shelter?.id != shelterId)
            return Forbidden(error = messageService["error.ShelterForbidden.message"]).build()

        val shelter = shelterService.getByIdWithAnimals(shelterId)
            ?: return NotFound(error = messageService["error.NonExistentShelter.message", shelterId]).build()

        val animal = shelter.animals.find { it.id == animalId }
            ?: return NotFound(error = messageService["error.NonExistentAnimal.message", animalId]).build()

        animalService.delete(animal)
        return Response(httpStatus = HttpStatus.NO_CONTENT).build()
    }

    // endregion
    // region Animal.photos

    @GetMapping("/{shelterId}/animal/{animalId}/photo/all")
    fun getAllAnimalPhotos(
        @PathVariable shelterId: Long,
        @PathVariable animalId: Long,
    ): ResponseEntity<*> {
        val shelter = shelterService.getByIdWithAnimals(shelterId)
            ?: return NotFound(error = messageService["error.NonExistentShelter.message", shelterId]).build()

        val animal = shelter.animals.find { it.id == animalId }
            ?: return NotFound(error = messageService["error.NonExistentAnimal.message", animalId]).build()

        return animal.photos.toImageArrayDTO().build()
    }

    @GetMapping("/{shelterId}/animal/{animalId}/photo/{photoId}")
    fun getAnimalPhotoById(
        @PathVariable shelterId: Long,
        @PathVariable animalId: Long,
        @PathVariable photoId: Long,
    ): ResponseEntity<Response> {
        val shelter = shelterService.getByIdWithAnimals(shelterId)
            ?: return NotFound(error = messageService["error.NonExistentShelter.message", shelterId]).build()

        val animal = shelter.animals.find { it.id == animalId }
            ?: return NotFound(error = messageService["error.NonExistentAnimal.message", animalId]).build()

        val photo = animal.photos.find { it.id == photoId }
            ?: return NotFound(error = messageService["error.NonExistentPhoto.message", photoId]).build()

        return photo.toImageDTO().build()
    }

    @PostMapping("/{shelterId}/animal/{animalId}/photo")
    fun addAnimalPhoto(
        @PathVariable shelterId: Long,
        @PathVariable animalId: Long,
        @Valid @RequestBody dto: AddAnimalPhotoDTO,
    ): ResponseEntity<*> {
        val user = SecurityContextHolder.getContext().authentication.principal
        if (user is Account && user.shelter?.id != shelterId)
            return Forbidden(error = messageService["error.ShelterForbidden.message"]).build()

        val shelter = shelterService.getByIdWithAnimals(shelterId)
            ?: return NotFound(error = messageService["error.NonExistentShelter.message", shelterId]).build()

        val animal = shelter.animals.find { it.id == animalId }
            ?: return NotFound(error = messageService["error.NonExistentAnimal.message", animalId]).build()

        return animalService.addPhoto(animal, imageService.create(Image.Type.PHOTO, dto.reference!!))
            .photos.toImageArrayDTO().build()
    }

    @DeleteMapping("/{shelterId}/animal/{animalId}/photo/{photoId}")
    fun removeAnimalPhotoById(
        @PathVariable shelterId: Long,
        @PathVariable animalId: Long,
        @PathVariable photoId: Long,
    ): ResponseEntity<Response> {
        val user = SecurityContextHolder.getContext().authentication.principal
        if (user is Account && user.shelter?.id != shelterId)
            return Forbidden(error = messageService["error.ShelterForbidden.message"]).build()

        val shelter = shelterService.getByIdWithAnimals(shelterId)
            ?: return NotFound(error = messageService["error.NonExistentShelter.message", shelterId]).build()

        val animal = shelter.animals.find { it.id == animalId }
            ?: return NotFound(error = messageService["error.NonExistentAnimal.message", animalId]).build()

        val photo = animal.photos.find { it.id == photoId }
            ?: return NotFound(error = messageService["error.NonExistentPhoto.message", photoId]).build()

        animalService.removePhoto(animal, photo)
        return Response(httpStatus = HttpStatus.NO_CONTENT).build()
    }

    // endregion
}
