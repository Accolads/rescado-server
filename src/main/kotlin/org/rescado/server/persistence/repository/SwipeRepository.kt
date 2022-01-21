package org.rescado.server.persistence.repository

import org.rescado.server.persistence.entity.Account
import org.rescado.server.persistence.entity.Animal
import org.rescado.server.persistence.entity.Swipe
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.ZonedDateTime

@Repository
interface SwipeRepository : JpaRepository<Swipe, Long> {

    fun existsByAccountAndAnimal(account: Account, animal: Animal): Boolean

    fun deleteByAccount(account: Account)

    fun deleteByTimestamp(timestamp: ZonedDateTime)
}
