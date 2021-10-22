package org.rescado.server.service

import org.rescado.server.persistence.entity.Account
import org.rescado.server.persistence.entity.Animal
import org.rescado.server.persistence.entity.Like
import org.rescado.server.persistence.repository.LikeRepository
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import javax.transaction.Transactional

@Service
@Transactional
class LikeService(
    private val likeRepository: LikeRepository,
) {

    fun getByAccount(account: Account) = likeRepository.findAllByAccount(account)

    fun create(account: Account, animal: Animal): Like {
        val like = Like(
            account = account,
            animal = animal,
            timestamp = ZonedDateTime.now(),
            reference = null,
            unreadCount = 0,
        )
        return likeRepository.save(like)
    }

    fun update(like: Like, reference: String?, unreadCount: Int?): Like {
        reference?.let { like.reference = it }
        unreadCount?.let { like.unreadCount = it }

        return likeRepository.save(like)
    }

    fun delete(like: Like) = likeRepository.delete(like)
}
