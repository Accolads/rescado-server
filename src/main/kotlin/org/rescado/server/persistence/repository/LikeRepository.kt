package org.rescado.server.persistence.repository

import org.rescado.server.persistence.entity.Account
import org.rescado.server.persistence.entity.Animal
import org.rescado.server.persistence.entity.Like
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LikeRepository : JpaRepository<Like, Long> {

    fun findAllByAccount(account: Account): List<Like>

    fun findAllByAnimal(animal: Animal): List<Like>
}
