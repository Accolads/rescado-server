package org.rescado.server.controller

import org.rescado.server.controller.dto.build
import org.rescado.server.controller.dto.req.AddAdminDTO
import org.rescado.server.controller.dto.req.AddVolunteerDTO
import org.rescado.server.controller.dto.res.Response
import org.rescado.server.controller.dto.res.error.BadRequest
import org.rescado.server.controller.dto.res.error.Forbidden
import org.rescado.server.controller.dto.toAccountDTO
import org.rescado.server.controller.dto.toAdminDTO
import org.rescado.server.persistence.entity.Account
import org.rescado.server.persistence.entity.Admin
import org.rescado.server.service.AccountService
import org.rescado.server.service.AdminService
import org.rescado.server.service.MessageService
import org.rescado.server.service.ShelterService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/admin")
class AdminController(
    private val adminService: AdminService,
    private val accountService: AccountService,
    private val shelterService: ShelterService,
    private val messageService: MessageService,
) {

    @PostMapping
    fun add(
        @Valid @RequestBody dto: AddAdminDTO,
        res: BindingResult,
    ): ResponseEntity<Response> {
        val user = SecurityContextHolder.getContext().authentication.principal
        if (user !is Admin)
            return Forbidden(error = messageService["error.AdminForbidden.message"]).build()

        if (res.hasErrors())
            return BadRequest(errors = res.allErrors.map { it.defaultMessage as String }).build()

        return adminService.create(
            username = dto.username,
            password = dto.password,
        ).toAdminDTO().build(HttpStatus.CREATED)
    }

    @PostMapping("/volunteer")
    fun addVolunteer(
        @Valid @RequestBody dto: AddVolunteerDTO,
        res: BindingResult,
    ): ResponseEntity<Response> {
        val user = SecurityContextHolder.getContext().authentication.principal
        if (user !is Admin)
            return Forbidden(error = messageService["error.AdminForbidden.message"]).build()

        if (res.hasErrors())
            return BadRequest(errors = res.allErrors.map { it.defaultMessage as String }).build()

        val account = accountService.getById(dto.accountId)
            ?: return BadRequest(error = messageService["error.NonExistentAccount.message", dto.accountId]).build()
        if (account.status == Account.Status.ANONYMOUS)
            return BadRequest(error = messageService["error.AccountIsAnonymous.message", dto.accountId]).build()
        val shelter = shelterService.getById(dto.shelterId)
            ?: return BadRequest(error = messageService["error.NonExistentShelter.message", dto.shelterId]).build()
        if (account.shelter != null)
            return BadRequest(error = messageService["error.AccountIsAlreadyVolunteer.message", dto.accountId]).build()

        return accountService.setVolunteer(account, shelter).toAccountDTO().build()
    }

    @DeleteMapping("/volunteer/{id}")
    fun removeVolunteer(
        @PathVariable id: Long,
        res: BindingResult,
    ): ResponseEntity<Response> {
        val user = SecurityContextHolder.getContext().authentication.principal
        if (user !is Admin)
            return Forbidden(error = messageService["error.AdminForbidden.message"]).build()

        if (res.hasErrors())
            return BadRequest(errors = res.allErrors.map { it.defaultMessage as String }).build()

        val account = accountService.getById(id)
            ?: return BadRequest(error = messageService["error.NonExistentAccount.message", id]).build()
        if (account.status == Account.Status.ANONYMOUS)
            return BadRequest(error = messageService["error.AccountIsAnonymous.message", id]).build()
        if (account.shelter == null)
            return BadRequest(error = messageService["error.AccountIsNotVolunteer.message", id]).build()

        return accountService.setVolunteer(account, null).toAccountDTO().build()
    }
}
