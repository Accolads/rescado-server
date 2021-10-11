package org.rescado.server.controller

import org.rescado.server.constant.SecurityConstants
import org.rescado.server.controller.dto.build
import org.rescado.server.controller.dto.req.AuthWithPasswordDTO
import org.rescado.server.controller.dto.req.AuthWithTokenDTO
import org.rescado.server.controller.dto.req.RegisterAccountDTO
import org.rescado.server.controller.dto.res.Response
import org.rescado.server.controller.dto.res.error.BadRequest
import org.rescado.server.controller.dto.res.error.Unauthorized
import org.rescado.server.controller.dto.toAuthenticationDTO
import org.rescado.server.controller.dto.toNewAccountDTO
import org.rescado.server.service.AccountService
import org.rescado.server.service.MessageService
import org.rescado.server.service.SessionService
import org.rescado.server.util.ClientAnalyzer
import org.rescado.server.util.PointGenerator
import org.rescado.server.util.generateAccessToken
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@RestController
@RequestMapping(SecurityConstants.AUTH_ROUTE)
class AuthenticationController(
    private val accountService: AccountService,
    private val sessionService: SessionService,
    private val messageService: MessageService,
    private val clientAnalyzer: ClientAnalyzer,
    private val pointGenerator: PointGenerator,
) {

    @PostMapping("/register")
    fun register(
        @RequestHeader(value = HttpHeaders.USER_AGENT) userAgent: String,
        @Valid @RequestBody dto: RegisterAccountDTO?,
        res: BindingResult,
        req: HttpServletRequest
    ): ResponseEntity<Response> {
        if (dto != null && dto.hasPartialCoordinates())
            return BadRequest(error = messageService["error.PartialCoordinates.message"]).build()
        if (res.hasErrors())
            return BadRequest(errors = res.allErrors.map { it.defaultMessage as String }).build()

        val account = accountService.create()
        val session = sessionService.create(
            account = account,
            agent = clientAnalyzer.getFromUserAgent(userAgent),
            ipAddress = req.remoteAddr,
            geometry = pointGenerator.make(dto?.latitude, dto?.longitude),
        )
        return account.toNewAccountDTO(generateAccessToken(account, session, req.serverName)).build()
    }

    @PostMapping("/login")
    fun authWithPassword(
        @RequestHeader(value = HttpHeaders.USER_AGENT) userAgent: String,
        @Valid @RequestBody dto: AuthWithPasswordDTO,
        res: BindingResult,
        req: HttpServletRequest
    ): ResponseEntity<Response> {
        if (res.hasErrors())
            return BadRequest(errors = res.allErrors.map { it.defaultMessage as String }).build()

        val account = accountService.getByEmailAndPassword(dto.email, dto.password)
            ?: return BadRequest(error = messageService["error.IncorrectCredentials.message"]).build()

        val session = sessionService.create(
            account = account,
            agent = clientAnalyzer.getFromUserAgent(userAgent),
            ipAddress = req.remoteAddr,
            geometry = pointGenerator.make(dto.latitude, dto.longitude),
        )
        return account.toAuthenticationDTO(generateAccessToken(account, session, req.serverName)).build()
    }

    @PostMapping("/refresh")
    fun authWithToken(
        @RequestHeader(value = HttpHeaders.USER_AGENT) userAgent: String,
        @Valid @RequestBody dto: AuthWithTokenDTO,
        res: BindingResult,
        req: HttpServletRequest
    ): ResponseEntity<Response> {
        if (res.hasErrors())
            return BadRequest(errors = res.allErrors.map { it.defaultMessage as String }).build()

        val account = accountService.getByUuid(dto.uuid)
            ?: return BadRequest(error = messageService["error.TokenMismatch.message"]).build() // don't tell account is registered

        var session = sessionService.getInitializedByToken(dto.token)
        if (session?.account != account) // token is null or token account does not match the requested account
            return BadRequest(error = messageService["error.TokenMismatch.message"]).build()

        session = sessionService.refresh(
            session = session,
            agent = clientAnalyzer.getFromUserAgent(userAgent),
            ipAddress = req.remoteAddr,
            geometry = pointGenerator.make(dto.latitude, dto.longitude),
        )
            ?: return Unauthorized(reason = Unauthorized.Reason.EXPIRED_REFRESH_TOKEN, realm = req.serverName).build()

        return account.toAuthenticationDTO(generateAccessToken(account, session, req.serverName)).build()
    }
}
