package org.rescado.server.util

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.locationtech.jts.geom.Point
import org.rescado.server.constant.SecurityConstants
import org.rescado.server.controller.dto.res.AccountDTO
import org.rescado.server.controller.dto.res.AnimalDTO
import org.rescado.server.controller.dto.res.AuthenticationDTO
import org.rescado.server.controller.dto.res.CoordinatesDTO
import org.rescado.server.controller.dto.res.Response
import org.rescado.server.controller.dto.res.SessionDTO
import org.rescado.server.controller.dto.res.ShelterDTO
import org.rescado.server.persistence.entity.Account
import org.rescado.server.persistence.entity.Animal
import org.rescado.server.persistence.entity.Session
import org.rescado.server.persistence.entity.Shelter
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.time.Duration
import java.time.Instant
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.Date

fun generateResponse(response: Response): ResponseEntity<Response> = ResponseEntity(response, response.httpHeaders, response.httpStatus)

fun generateResponse(response: List<Response>): ResponseEntity<List<Response>> = if (response.isEmpty()) ResponseEntity(response, HttpStatus.OK) else ResponseEntity(response, response.first().httpHeaders, response.first().httpStatus)

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

// region Point mappers

fun Point.toCoordinatesDTO() = CoordinatesDTO(
    latitude = y,
    longitude = x,
)
// endregion
// region Account mappers

fun Account.toAuthenticationDTO(authorization: String) = AuthenticationDTO(
    authorization = authorization,
    uuid = uuid,
    email = email,
    name = name
)

fun Account.toNewAccountDTO(authorization: String) = AuthenticationDTO(
    httpStatus = HttpStatus.CREATED,
    authorization = authorization,
    uuid = uuid,
    email = email,
    name = name
)

fun Account.toAccountDTO() = AccountDTO(
    uuid = uuid,
    email = email,
    name = name,
    status = status.name,
)
// endregion
// region Session mappers

fun Session.toSessionDTO() = SessionDTO(
    refreshToken = refreshToken,
    agent = agent,
    firstLogin = firstLogin,
    lastLogin = lastLogin,
    ipAddress = ipAddress,
    coordinates = geometry?.toCoordinatesDTO(),
)

fun List<Session>.toSessionArrayDTO() = this.map { it.toSessionDTO() }
// endregion
// region Shelter mappers

fun Shelter.toShelterDTO(shortVersion: Boolean) = if (shortVersion) ShelterDTO(
    id = id,
    name = name,
    email = null,
    website = null,
    newsfeed = null,
    address = null,
    postalCode = null,
    city = city,
    country = country,
    coordinates = geometry.toCoordinatesDTO(),
    logo = logo.url,
    banner = null,
) else ShelterDTO(
    id = id,
    name = name,
    email = email,
    website = website,
    newsfeed = newsfeed,
    address = address,
    postalCode = postalCode,
    city = city,
    country = country,
    coordinates = geometry.toCoordinatesDTO(),
    logo = logo.url,
    banner = banner?.url,
)
// endregion
// region Animal mappers

fun Animal.toAnimalDTO(now: ZonedDateTime) = AnimalDTO(
    id = id,
    name = name,
    kind = kind.name,
    breed = breed,
    sex = sex.name,
    age = (Duration.between(birthday, now).toDays() / 365).toInt(),
    weight = weight,
    vaccinated = vaccinated,
    sterilized = sterilized,
    photos = photos.map { it.url },
    shelter = shelter.toShelterDTO(true),
)

fun List<Animal>.toAnimalArrayDTO(now: ZonedDateTime) = this.map { it.toAnimalDTO(now) }
// endregion
