package org.rescado.server.service

import org.rescado.server.constant.SecurityConstants
import org.rescado.server.persistence.entity.Account
import org.rescado.server.persistence.entity.Animal
import org.rescado.server.persistence.repository.CardsRepository
import org.rescado.server.util.AreaData
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class CardsService(
    private val cardsRepository: CardsRepository,
) {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    fun getCards(
        account: Account,
        location: AreaData,
        kinds: List<Animal.Kind>,
        sexes: List<Animal.Sex>,
        minimumAge: Int?,
        maximumAge: Int?,
        minimumWeight: Int?,
        maximumWeight: Int?,
        vaccinated: Boolean?,
        sterilized: Boolean?,
    ): List<Animal> {
        return cardsRepository.findCards(
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
    }
}
