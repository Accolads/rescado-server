package org.rescado.server.service

import org.hibernate.Hibernate
import org.rescado.server.persistence.entity.Animal
import org.rescado.server.persistence.entity.Image
import org.rescado.server.persistence.entity.Shelter
import org.rescado.server.persistence.repository.AnimalRepository
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import javax.transaction.Transactional

@Service
@Transactional
class AnimalService(
    private val animalRepository: AnimalRepository,
    private val imageService: ImageService,
) {

    fun getById(id: Long): Animal? = animalRepository.findById(id).orElse(null)

    fun getAll(): List<Animal> {
        val animals = animalRepository.findAll()
        animals.forEach {
            Hibernate.initialize(it.photos)
            Hibernate.initialize(it.shelter)
            Hibernate.initialize(it.shelter.logo)
            Hibernate.initialize(it.shelter.banner)
        }
        return animals
    }

    fun create(
        shelter: Shelter,
        kind: Animal.Kind,
        breed: String,
        name: String,
        description: String,
        sex: Animal.Sex,
        birthday: ZonedDateTime,
        weight: Int,
        vaccinated: Boolean,
        sterilized: Boolean,
        photos: MutableSet<Image>,
    ): Animal {
        val animal = Animal(
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
            photos = photos,
            likes = mutableSetOf(),
        )
        return animalRepository.save(animal)
    }

    fun update(
        animal: Animal,
        kind: Animal.Kind?,
        breed: String?,
        name: String?,
        description: String?,
        sex: Animal.Sex?,
        birthday: ZonedDateTime?,
        weight: Int?,
        vaccinated: Boolean?,
        sterilized: Boolean?,
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

        return animalRepository.save(animal)
    }

    fun addPhoto(animal: Animal, photo: Image) {
        animal.photos.add(photo)
        animalRepository.save(animal)
    }

    fun removePhoto(animal: Animal, photo: Image) {
        imageService.delete(photo)
        animal.photos.remove(photo)
        animalRepository.save(animal)
    }

    fun delete(animal: Animal) = animalRepository.delete(animal)
}
