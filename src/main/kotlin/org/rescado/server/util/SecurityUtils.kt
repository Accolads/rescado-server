package org.rescado.server.util

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.rescado.server.constant.SecurityConstants
import org.rescado.server.persistence.entity.Account
import org.rescado.server.persistence.entity.Session
import org.springframework.security.crypto.bcrypt.BCrypt
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date

fun hashPassword(password: String): String = BCrypt.hashpw(password, SecurityConstants.SALT)

fun checkPassword(password: String, hashedPassword: String) = BCrypt.checkpw(password, hashedPassword)

fun generateAccessToken(account: Account, session: Session, serverName: String): String {
    val now = Instant.now()
    val jwt = Jwts.builder()
        .setHeaderParam("typ", SecurityConstants.TOKEN_TYPE)
        .setIssuer("$serverName${SecurityConstants.AUTH_ROUTE}")
        .setAudience(serverName)
        .setSubject(account.uuid)
        .setExpiration(Date.from(now.plus(SecurityConstants.TOKEN_TTL, ChronoUnit.HOURS)))
        .setIssuedAt(Date.from(now))
        .setNotBefore(Date.from(now))
        .claim("status", account.status.name)
        .claim("device", session.device)
        .claim("agent", session.agent)
        .claim("ipAddress", session.ipAddress)
        .claim("refreshToken", session.refreshToken)
        .claim("refreshExpiry", session.lastLogin.plus(SecurityConstants.REFRESH_TTL, ChronoUnit.HOURS).toEpochSecond())
        .signWith(Keys.hmacShaKeyFor(SecurityConstants.SECRET.toByteArray()))
        .compact()
    return "${SecurityConstants.TOKEN_PREFIX}$jwt"
}
