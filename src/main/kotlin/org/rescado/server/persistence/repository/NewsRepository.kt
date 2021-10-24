package org.rescado.server.persistence.repository

import org.rescado.server.persistence.entity.News
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NewsRepository : JpaRepository<News, Long> {

    fun findAllByReferenceOrderByTimestampDesc(reference: Long): List<News>
}
