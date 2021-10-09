package org.rescado.server.util

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.rescado.server.constant.SecurityConstants
import org.rescado.server.controller.dto.res.AccountDTO
import org.rescado.server.controller.dto.res.AuthenticationDTO
import org.rescado.server.controller.dto.res.Response
import org.rescado.server.controller.dto.res.SessionDTO
import org.rescado.server.controller.dto.res.SessionDTOsession
import org.rescado.server.persistence.entity.Account
import org.rescado.server.persistence.entity.Session
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date

fun generateResponse(response: Response): ResponseEntity<Response> {
    return ResponseEntity(response, response.httpHeaders, response.httpStatus)
}

fun generateAccessToken(account: Account, session: Session, serverName: String): String {
    val now = Instant.now()
    val jwt = Jwts.builder()
        .signWith(Keys.hmacShaKeyFor(SecurityConstants.JWT_SECRET.toByteArray()))
        .setHeaderParam("typ", SecurityConstants.TOKEN_TYPE)
        .setIssuer("$serverName${SecurityConstants.AUTH_ROUTE}")
        .setAudience(serverName)
        .setSubject(account.uuid)
        .setExpiration(Date.from(now.plus(SecurityConstants.TOKEN_TTL, ChronoUnit.HOURS)))
        .setIssuedAt(Date.from(now))
        .setNotBefore(Date.from(now))
        .claim("account", account.email)
        .claim("agent", session.agent)
        .claim("refresh_token", session.refreshToken)
        .claim("refresh_expiry", session.lastLogin.plus(SecurityConstants.REFRESH_TTL, ChronoUnit.HOURS).toEpochSecond())
        .compact()
    return "${SecurityConstants.TOKEN_PREFIX}$jwt"
}

fun Account.toAuthenticationResponse(authorization: String) = AuthenticationDTO(
    authorization = authorization,
    uuid = uuid,
    email = email,
    name = name
)

fun Account.toNewAccountResponse(authorization: String) = AuthenticationDTO(
    httpStatus = HttpStatus.CREATED,
    authorization = authorization,
    uuid = uuid,
    email = email,
    name = name
)

fun Account.toAccountResponse() = AccountDTO(
    uuid = uuid,
    email = email,
    name = name,
    status = status.name,
)

fun List<Session>.toSessionResponse() = SessionDTO(
    sessions = this.map {
        SessionDTOsession(
            it.refreshToken,
            it.agent,
            it.firstLogin,
            it.lastLogin,
        )
    }
)
