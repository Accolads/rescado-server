package org.rescado.server.service

import org.rescado.server.persistence.entity.Animal
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

    fun getByReferences(animals: Collection<Animal>) = newsRepository.findAllByAnimalInOrderByTimestampDesc(animals)

    fun create(type: News.Type, animal: Animal): News {
        val news = News(
            type = type,
            animal = animal,
            timestamp = ZonedDateTime.now(),
        )
        return newsRepository.save(news)
    }
}
