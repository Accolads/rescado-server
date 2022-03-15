package org.rescado.server.controller

import org.rescado.server.controller.dto.build
import org.rescado.server.controller.dto.req.JoinGroupDTO
import org.rescado.server.controller.dto.res.LikeDTO
import org.rescado.server.controller.dto.res.Response
import org.rescado.server.controller.dto.res.error.BadRequest
import org.rescado.server.controller.dto.res.error.Forbidden
import org.rescado.server.controller.dto.toAnimalDTO
import org.rescado.server.persistence.entity.Account
import org.rescado.server.persistence.entity.Membership
import org.rescado.server.service.CardService
import org.rescado.server.service.GroupService
import org.rescado.server.service.MessageService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/groups")
class GroupController(
    private val cardService: CardService,
    private val groupService: GroupService,
    private val messageService: MessageService,
) {
    // region groups

    @PostMapping
    fun create(): ResponseEntity<Response> {
        val user = SecurityContextHolder.getContext().authentication.principal
        if (user !is Account)
            return Forbidden(error = messageService["error.ResourceForbidden.message"]).build()

        if (user.memberships.firstOrNull { it.status == Membership.Status.CONFIRMED } != null)
            return BadRequest(error = messageService["error.AlreadyInGroup.message"]).build()

        groupService.create(user)

        return Response(httpStatus = HttpStatus.OK).build()
    }

    @PostMapping("/{groupId}")
    fun join(
        @PathVariable groupId: Long,
        @Valid @RequestBody dto: JoinGroupDTO,
        res: BindingResult,
    ): ResponseEntity<Response> {
        val user = SecurityContextHolder.getContext().authentication.principal
        if (user !is Account)
            return Forbidden(error = messageService["error.ResourceForbidden.message"]).build()

        if (res.hasErrors())
            return BadRequest(errors = res.allErrors.map { it.defaultMessage as String }).build()

        if (user.memberships.firstOrNull { it.status == Membership.Status.CONFIRMED } != null)
            return BadRequest(error = messageService["error.AlreadyInGroup.message"]).build()

        val group = groupService.isValidInvite(dto.uuid!!, groupId)
            ?: return Forbidden(error = messageService["error.NotInvited.message"]).build()

        groupService.join(user, group)

        return Response(httpStatus = HttpStatus.OK).build()
    }

    @PatchMapping("{/groupId")
    fun switch(
        @PathVariable groupId: Long,
    ): ResponseEntity<Response> {
        val user = SecurityContextHolder.getContext().authentication.principal
        if (user !is Account)
            return Forbidden(error = messageService["error.ResourceForbidden.message"]).build()

        val membership = user.memberships.firstOrNull { it.group.id == groupId }
            ?: return Forbidden(error = messageService["error.NotInvited.message"]).build()

        if (membership.status == Membership.Status.CONFIRMED)
            return BadRequest(error = messageService["error.AlreadyInGroup.message"]).build()

        groupService.join(user, membership.group)

        return Response(httpStatus = HttpStatus.OK).build()
    }

    @DeleteMapping("/{groupId}")
    fun leave(
        @PathVariable groupId: Long,
    ): ResponseEntity<Response> {
        val user = SecurityContextHolder.getContext().authentication.principal
        if (user !is Account)
            return Forbidden(error = messageService["error.ResourceForbidden.message"]).build()

        val membership = user.memberships.firstOrNull { it.group.id == groupId }
            ?: return BadRequest(error = messageService["error.NotInGroup.message"]).build()

        groupService.deleteMembership(membership)
        return Response(httpStatus = HttpStatus.NO_CONTENT).build()
    }

    // endregion
    // region likes

    @GetMapping("liked")
    fun getLiked(
        @RequestParam detailed: Boolean = false,
    ): ResponseEntity<*> {
        val user = SecurityContextHolder.getContext().authentication.principal
        if (user !is Account)
            return Forbidden(error = messageService["error.ResourceForbidden.message"]).build()

        val membership = user.memberships.firstOrNull { it.status == Membership.Status.CONFIRMED }
            ?: return BadRequest(error = messageService["error.NotInGroup.message"]).build()

        if (detailed)
            return membership.group.memberships
                .map {
                    cardService.getLikedByAccountWithAnimals(it.account).toMutableList()
                }
                .reduce { all, it ->
                    all.apply { retainAll(it) }
                }
                .map {
                    LikeDTO(
                        timestamp = it.timestamp,
                        reference = null, // TODO Figure out a way to implement group chat or skip this feature altogether
                        animal = it.animal.toAnimalDTO()
                    )
                }.build()
        return ResponseEntity(
            membership.group.memberships
                .map {
                    cardService.getLikedByAccountWithAnimals(it.account).toMutableList()
                }
                .reduce { all, it ->
                    all.apply { retainAll(it) }
                }
                .map {
                    it.animal.id
                },
            HttpStatus.OK
        )
    }

    // endregion
}