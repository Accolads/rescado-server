package org.rescado.server.persistence.repository

import org.rescado.server.persistence.entity.Animal
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AnimalRepository : JpaRepository<Animal, Long>
