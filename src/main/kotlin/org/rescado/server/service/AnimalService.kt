package org.rescado.server.service

import org.rescado.server.constant.SecurityConstants
import org.rescado.server.persistence.entity.Animal
import org.rescado.server.persistence.entity.Image
import org.rescado.server.persistence.entity.News
import org.rescado.server.persistence.entity.Shelter
import org.rescado.server.persistence.repository.AnimalRepository
import org.rescado.server.service.exception.PhotoMaximumLimitReachedException
import org.rescado.server.service.exception.PhotoMinimumLimitReachedException
import org.springframework.stereotype.Service
import java.time.LocalDate
import javax.transaction.Transactional

@Service
@Transactional
class AnimalService(
    private val animalRepository: AnimalRepository,
    private val imageService: ImageService,
    private val newsService: NewsService,
) {

    fun getById(id: Long): Animal? = animalRepository.findById(id).orElse(null)

    fun create(
        shelter: Shelter,
        kind: Animal.Kind,
        breed: String,
        name: String,
        description: String,
        sex: Animal.Sex,
        birthday: LocalDate,
        weight: Int,
        vaccinated: Boolean,
        sterilized: Boolean,
        availability: Animal.Availability,
        photos: MutableSet<Image>,
    ): Animal {
        val animal = animalRepository.save(
            Animal(
                shelter = shelter,
                kind = kind,
                breed = breed,
                name = name,
                description = description,
                sex = sex,
                birthday = birthday,
                weight = weight,
                vaccinated = vaccinated,
                sterilized = sterilized,
                availability = availability,
                photos = photos,
                likes = mutableSetOf(),
            )
        )
        if (animal.availability == Animal.Availability.AVAILABLE)
            newsService.create(News.Type.SHELTER_NEW_ANIMAL, animal)
        return animal
    }

    fun update(
        animal: Animal,
        kind: Animal.Kind?,
        breed: String?,
        name: String?,
        description: String?,
        sex: Animal.Sex?,
        birthday: LocalDate?,
        weight: Int?,
        vaccinated: Boolean?,
        sterilized: Boolean?,
        availability: Animal.Availability?,
    ): Animal {
        kind?.let { animal.kind = it }
        breed?.let { animal.breed = it }
        name?.let { animal.name = it }
        description?.let { animal.description = it }
        sex?.let { animal.sex = it }
        birthday?.let { animal.birthday = it }
        weight?.let { animal.weight = it }
        vaccinated?.let { animal.vaccinated = it }
        sterilized?.let { animal.sterilized = it }
        availability?.let {
            when (availability) {
                Animal.Availability.AVAILABLE -> newsService.create(News.Type.SHELTER_AVAILABLE_ANIMAL, animal)
                Animal.Availability.ADOPTED -> newsService.create(News.Type.ANIMAL_ADOPTED, animal)
                else -> Unit
            }
            animal.availability = it
        }

        return animalRepository.save(animal)
    }

    fun addPhoto(animal: Animal, photo: Image): Animal {
        if (animal.photos.size >= SecurityConstants.IMAGE_MAX_REFERENCES)
            throw PhotoMaximumLimitReachedException(SecurityConstants.IMAGE_MAX_REFERENCES)
        animal.photos.add(photo)
        animalRepository.save(animal)
        newsService.create(News.Type.ANIMAL_NEW_PHOTO, animal)
        return animal
    }

    fun removePhoto(animal: Animal, photo: Image): Animal {
        if (animal.photos.size == 1)
            throw PhotoMinimumLimitReachedException(1)
        imageService.delete(photo)
        animal.photos.remove(photo)
        animalRepository.save(animal)
        return animal
    }

    fun delete(animal: Animal) = animalRepository.delete(animal)
}
