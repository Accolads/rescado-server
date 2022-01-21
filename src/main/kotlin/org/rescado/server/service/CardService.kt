package org.rescado.server.service

import org.hibernate.Hibernate
import org.rescado.server.persistence.entity.Account
import org.rescado.server.persistence.entity.Animal
import org.rescado.server.persistence.entity.Like
import org.rescado.server.persistence.entity.Swipe
import org.rescado.server.persistence.repository.CardsRepository
import org.rescado.server.persistence.repository.LikeRepository
import org.rescado.server.persistence.repository.SwipeRepository
import org.rescado.server.util.AreaData
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import javax.transaction.Transactional

@Service
@Transactional
class CardService(
    private val cardsRepository: CardsRepository,
    private val swipeRepository: SwipeRepository,
    private val likeRepository: LikeRepository,
) {
    // private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    // region generation

    fun createCards(
        account: Account,
        number: Int,
        location: AreaData,
        kinds: Set<Animal.Kind>,
        sexes: Set<Animal.Sex>,
        minimumAge: Int?,
        maximumAge: Int?,
        minimumWeight: Int?,
        maximumWeight: Int?,
        vaccinated: Boolean?,
        sterilized: Boolean?,
    ): List<Animal> = cardsRepository.findCards(
        account = account,
        limit = number,
        location = location,
        kinds = kinds,
        sexes = sexes,
        minimumAge = minimumAge,
        maximumAge = maximumAge,
        minimumWeight = minimumWeight,
        maximumWeight = maximumWeight,
        vaccinated = vaccinated,
        sterilized = sterilized,
    )

    // endregion
    // region likes

    fun checkIfLikeExists(account: Account, animal: Animal) = likeRepository.existsByAccountAndAnimal(account, animal)

    fun getLikedByAccount(account: Account) = likeRepository.findAllByAccount(account)

    fun getLikedByAccountWithAnimals(account: Account) = likeRepository.findAllByAccount(account).onEach {
        Hibernate.initialize(it.animal)
    }

    fun createLike(account: Account, animal: Animal): Like {
        val like = Like(
            account = account,
            animal = animal,
            timestamp = ZonedDateTime.now(),
            reference = null,
            unreadCount = 0,
        )
        return likeRepository.save(like)
    }

    fun updateLike(like: Like, reference: String?, unreadCount: Int?): Like {
        reference?.let { like.reference = it }
        unreadCount?.let { like.unreadCount = it }

        return likeRepository.save(like)
    }

    fun deleteLike(account: Account, animal: Animal) = likeRepository.deleteByAccountAndAnimal(account, animal)

    // endregion
    // region skipped

    fun checkIfSwipeExists(account: Account, animal: Animal) = swipeRepository.existsByAccountAndAnimal(account, animal)

    fun createSwipe(account: Account, animal: Animal): Swipe {
        val swipe = Swipe(
            account = account,
            animal = animal,
            timestamp = ZonedDateTime.now(),
        )
        return swipeRepository.save(swipe)
    }

    fun deleteSwiped(account: Account) = swipeRepository.deleteByAccount(account)

// TODO implement
// @Scheduled(cron = daily?)
// fun cleanup(){
//     swipeRepository.deleteByTimestamp( older than X days )
// }
}
