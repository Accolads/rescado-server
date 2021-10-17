package org.rescado.server.service

import org.hibernate.Hibernate
import org.rescado.server.persistence.entity.Animal
import org.rescado.server.persistence.repository.AnimalRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class AnimalService(
    private val animalRepository: AnimalRepository,
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
}
