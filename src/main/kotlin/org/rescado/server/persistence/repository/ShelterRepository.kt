package org.rescado.server.persistence.repository

import org.rescado.server.persistence.entity.Shelter
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ShelterRepository : JpaRepository<Shelter, Long>
