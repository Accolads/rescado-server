package org.rescado.server.service

import org.rescado.server.constant.SecurityConstants
import org.rescado.server.persistence.entity.Account
import org.rescado.server.persistence.entity.Animal
import org.rescado.server.persistence.entity.Swipe
import org.rescado.server.persistence.repository.CardsRepository
import org.rescado.server.persistence.repository.SwipeRepository
import org.rescado.server.util.AreaData
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import javax.transaction.Transactional

@Service
@Transactional
class SwipeService(
    private val cardsRepository: CardsRepository,
    private val swipeRepository: SwipeRepository,
) {
    // private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    fun createBatch(account: Account, animals: List<Animal>): List<Swipe> {
        val now = ZonedDateTime.now()
        return swipeRepository.saveAll(
            animals.map {
                Swipe(
                    account = account,
                    animal = it,
                    timestamp = now,
                )
            }
        )
    }

    fun createCards(
        account: Account,
        location: AreaData,
        kinds: Set<Animal.Kind>,
        sexes: Set<Animal.Sex>,
        minimumAge: Int?,
        maximumAge: Int?,
        minimumWeight: Int?,
        maximumWeight: Int?,
        vaccinated: Boolean?,
        sterilized: Boolean?,
    ): List<Animal> {
        return createBatch(
            account,
            cardsRepository.findCards(
                account = account,
                limit = SecurityConstants.CARDS_LIMIT,
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
        ).map { it.animal }
    }

    fun reset(account: Account) = swipeRepository.deleteByAccount(account)

    // TODO implement
    // @Scheduled(cron = daily?)
    // fun cleanup(){
    //     swipeRepository.deleteByTimestamp( older than X days )
    // }
}
