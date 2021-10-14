package org.rescado.server.controller

import org.rescado.server.controller.dto.build
import org.rescado.server.controller.dto.req.AddAdminDTO
import org.rescado.server.controller.dto.res.Response
import org.rescado.server.controller.dto.res.error.BadRequest
import org.rescado.server.controller.dto.res.error.Forbidden
import org.rescado.server.controller.dto.toAdminDTO
import org.rescado.server.persistence.entity.Admin
import org.rescado.server.service.AdminService
import org.rescado.server.service.MessageService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/admin")
class AdminController(
    private val adminService: AdminService,
    private val messageService: MessageService,
) {

    @PostMapping
    fun add(
        @Valid @RequestBody dto: AddAdminDTO,
        res: BindingResult,
    ): ResponseEntity<Response> {
        val user = SecurityContextHolder.getContext().authentication.principal
        if (user !is Admin)
            return Forbidden(error = messageService["error.AdminForbidden.message"]).build() // TODO add label

        if (res.hasErrors())
            return BadRequest(errors = res.allErrors.map { it.defaultMessage as String }).build()

        return adminService.create(
            username = dto.username,
            password = dto.password,
        ).toAdminDTO().build(HttpStatus.CREATED)
    }

    @PostMapping("/xx")
    fun x(): String {
        return "Hello"
    }
}
