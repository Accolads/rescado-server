package org.rescado.server.controller

import org.rescado.server.constant.SecurityConstants
import org.rescado.server.controller.dto.build
import org.rescado.server.controller.dto.req.AuthAnonymouslyDTO
import org.rescado.server.controller.dto.req.AuthWithPasswordDTO
import org.rescado.server.controller.dto.req.AuthWithTokenDTO
import org.rescado.server.controller.dto.req.RegisterAccountDTO
import org.rescado.server.controller.dto.res.Response
import org.rescado.server.controller.dto.res.error.BadRequest
import org.rescado.server.controller.dto.res.error.Unauthorized
import org.rescado.server.controller.dto.toAuthenticationDTO
import org.rescado.server.controller.dto.toNewAccountDTO
import org.rescado.server.persistence.entity.Account
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
        @RequestHeader(value = SecurityConstants.DEVICE_HEADER) device: String?,
        @RequestHeader(value = HttpHeaders.USER_AGENT) agent: String,
        @Valid @RequestBody dto: RegisterAccountDTO?,
        res: BindingResult,
        req: HttpServletRequest,
    ): ResponseEntity<Response> {
        if (dto != null && dto.hasPartialCoordinates())
            return BadRequest(error = messageService["error.PartialCoordinates.message"]).build()
        if (res.hasErrors())
            return BadRequest(errors = res.allErrors.map { it.defaultMessage as String }).build()

        val client = clientAnalyzer.analyze(device, agent)
        val account = accountService.create()
        val session = sessionService.create(
            account = account,
            device = client.first,
            agent = client.second,
            ipAddress = req.remoteAddr,
            geometry = pointGenerator.make(dto?.latitude, dto?.longitude),
        )
        return account.toNewAccountDTO(generateAccessToken(account, session, req.serverName)).build()
    }

    @PostMapping("/login")
    fun authWithPassword(
        @RequestHeader(value = SecurityConstants.DEVICE_HEADER) device: String?,
        @RequestHeader(value = HttpHeaders.USER_AGENT) agent: String,
        @Valid @RequestBody dto: AuthWithPasswordDTO,
        res: BindingResult,
        req: HttpServletRequest,
    ): ResponseEntity<Response> {
        if (res.hasErrors())
            return BadRequest(errors = res.allErrors.map { it.defaultMessage as String }).build()

        val account = accountService.getByEmailAndPassword(dto.email!!, dto.password!!)
            ?: return BadRequest(error = messageService["error.CredentialsMismatch.message"]).build()

        val client = clientAnalyzer.analyze(device, agent)
        val session = sessionService.create(
            account = account,
            device = client.first,
            agent = client.second,
            ipAddress = req.remoteAddr,
            geometry = pointGenerator.make(dto.latitude, dto.longitude),
        )
        return account.toAuthenticationDTO(generateAccessToken(account, session, req.serverName)).build()
    }

    @PostMapping("/recover")
    fun authAnonymously(
        @RequestHeader(value = SecurityConstants.DEVICE_HEADER) device: String?,
        @RequestHeader(value = HttpHeaders.USER_AGENT) agent: String,
        @Valid @RequestBody dto: AuthAnonymouslyDTO,
        res: BindingResult,
        req: HttpServletRequest,
    ): ResponseEntity<Response> {
        if (res.hasErrors())
            return BadRequest(errors = res.allErrors.map { it.defaultMessage as String }).build()

        val account = accountService.getByUuid(dto.uuid!!)
            ?: return BadRequest(error = messageService["error.NotAnonymous.message"]).build() // don't tell account isn't registered
        if (account.status != Account.Status.ANONYMOUS)
            return BadRequest(error = messageService["error.NotAnonymous.message"]).build()

        val client = clientAnalyzer.analyze(device, agent)
        val session = sessionService.create(
            account = account,
            device = client.first,
            agent = client.second,
            ipAddress = req.remoteAddr,
            geometry = pointGenerator.make(dto.latitude, dto.longitude),
        )
        return account.toAuthenticationDTO(generateAccessToken(account, session, req.serverName)).build()
    }

    @PostMapping("/refresh")
    fun authWithToken(
        @RequestHeader(value = SecurityConstants.DEVICE_HEADER) device: String?,
        @RequestHeader(value = HttpHeaders.USER_AGENT) agent: String,
        @Valid @RequestBody dto: AuthWithTokenDTO,
        res: BindingResult,
        req: HttpServletRequest,
    ): ResponseEntity<Response> {
        if (res.hasErrors())
            return BadRequest(errors = res.allErrors.map { it.defaultMessage as String }).build()

        val account = accountService.getByUuid(dto.uuid!!)
            ?: return BadRequest(error = messageService["error.TokenMismatch.message"]).build() // don't tell account isn't registered

        var session = sessionService.getInitializedByToken(
            dto.token!!
        )
        if (session?.account != account) // token is null or token account does not match the requested account
            return BadRequest(error = messageService["error.TokenMismatch.message"]).build()

        val client = clientAnalyzer.analyze(device, agent)
        session = sessionService.refresh(
            session = session,
            device = client.first,
            agent = client.second,
            ipAddress = req.remoteAddr,
            geometry = pointGenerator.make(dto.latitude, dto.longitude),
        )
            ?: return Unauthorized(error = messageService["error.TokenExpired.message"], reason = Unauthorized.Reason.EXPIRED_REFRESH_TOKEN, realm = req.serverName).build()

        return account.toAuthenticationDTO(generateAccessToken(account, session, req.serverName)).build()
    }
}
