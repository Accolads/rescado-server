package org.rescado.server.service

import org.rescado.server.persistence.entity.News
import org.rescado.server.persistence.repository.NewsRepository
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import javax.transaction.Transactional

@Service
@Transactional
class NewsService(
    private val newsRepository: NewsRepository,
) {

    // TODO add scheduled job to remove old news from the database

    fun getByReference(reference: Long) = newsRepository.findAllByReferenceOrderByTimestampDesc(reference)

    fun create(type: News.Type, reference: Long): News {
        val news = News(
            type = type,
            reference = reference,
            timestamp = ZonedDateTime.now(),
        )
        return newsRepository.save(news)
    }
}
