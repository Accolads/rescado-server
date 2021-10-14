package org.rescado.server.controller

import org.rescado.server.constant.SecurityConstants
import org.rescado.server.controller.dto.build
import org.rescado.server.controller.dto.req.AddAdminDTO
import org.rescado.server.controller.dto.res.Response
import org.rescado.server.controller.dto.res.error.BadRequest
import org.rescado.server.controller.dto.toAdminDTO
import org.rescado.server.service.AdminService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@RestController
@RequestMapping(SecurityConstants.ADMIN_ROUTE)
class AdminController(
    private val adminService: AdminService,
) {

    @PostMapping
    fun add(
        @Valid @RequestBody dto: AddAdminDTO,
        res: BindingResult,
        req: HttpServletRequest,
    ): ResponseEntity<Response> {
        if (res.hasErrors())
            return BadRequest(errors = res.allErrors.map { it.defaultMessage as String }).build()

        return adminService.create(
            username = dto.username,
            password = dto.password
        ).toAdminDTO().build(HttpStatus.CREATED)
    }
}
