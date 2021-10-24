package org.rescado.server.controller

import org.rescado.server.controller.dto.build
import org.rescado.server.controller.dto.res.NewsDTO
import org.rescado.server.controller.dto.res.error.Forbidden
import org.rescado.server.controller.dto.toAnimalDTO
import org.rescado.server.controller.dto.toShelterDTO
import org.rescado.server.persistence.entity.Account
import org.rescado.server.service.AccountService
import org.rescado.server.service.LikeService
import org.rescado.server.service.MessageService
import org.rescado.server.service.NewsService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/news")
class NewsController(
    private val newsService: NewsService,
    private val likeService: LikeService,
    private val accountService: AccountService,
    private val messageService: MessageService,
) {

    @GetMapping
    fun get(): ResponseEntity<*> {
        val user = SecurityContextHolder.getContext().authentication.principal
        if (user !is Account)
            return Forbidden(error = messageService["error.NewsForbidden.message"]).build()

        val news = likeService.getByAccountWithAnimalsAndAnimalShelter(user).flatMap {
            newsService.getByReference(it.animal.id).map { news ->
                NewsDTO(
                    type = news.type.name,
                    timestamp = news.timestamp,
                    reference = it.animal.toAnimalDTO()
                )
            }
        } + accountService.getByIdWithFollowing(user.id)!!.following.flatMap {
            newsService.getByReference(it.id).map { news ->
                NewsDTO(
                    type = news.type.name,
                    timestamp = news.timestamp,
                    reference = it.toShelterDTO(true)
                )
            }
        }.sortedByDescending { it.timestamp }

        return news.build()
    }
}
