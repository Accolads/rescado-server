package org.rescado.server.persistence.repository

import org.rescado.server.persistence.entity.Animal
import org.rescado.server.persistence.entity.News
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NewsRepository : JpaRepository<News, Long> {

    fun findAllByAnimalInOrderByTimestampDesc(animals: Collection<Animal>): List<News>
}
