package org.rescado.server.persistence.repository

import org.rescado.server.persistence.entity.Swipe
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SwipeRepository : JpaRepository<Swipe, Long>
