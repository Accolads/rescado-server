package org.rescado.server.controller

import org.rescado.server.controller.dto.build
import org.rescado.server.controller.dto.req.PatchAccountDTO
import org.rescado.server.controller.dto.res.Response
import org.rescado.server.controller.dto.res.error.BadRequest
import org.rescado.server.controller.dto.res.error.Forbidden
import org.rescado.server.controller.dto.toAccountDTO
import org.rescado.server.persistence.entity.Account
import org.rescado.server.persistence.entity.Image
import org.rescado.server.service.AccountService
import org.rescado.server.service.ImageService
import org.rescado.server.service.MessageService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/account")
class AccountController(
    private val accountService: AccountService,
    private val imageService: ImageService,
    private val messageService: MessageService,
) {

    @PatchMapping
    fun patch(
        @Valid @RequestBody dto: PatchAccountDTO,
        res: BindingResult,
    ): ResponseEntity<Response> {
        val user = SecurityContextHolder.getContext().authentication.principal
        if (user !is Account)
            return Forbidden(error = messageService["error.AccountForbidden.message"]).build()

        if (res.hasErrors())
            return BadRequest(errors = res.allErrors.map { it.defaultMessage as String }).build()

        return accountService.update(
            account = user,
            name = dto.name,
            email = dto.name,
            password = dto.password,
            avatar = dto.avatar?.let { imageService.create(Image.Type.AVATAR, dto.avatar) },
        ).toAccountDTO().build()
    }
}
