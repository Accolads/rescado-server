package org.rescado.server.controller.dto

import org.locationtech.jts.geom.Point
import org.rescado.server.controller.dto.res.AccountDTO
import org.rescado.server.controller.dto.res.AdminDTO
import org.rescado.server.controller.dto.res.AnimalDTO
import org.rescado.server.controller.dto.res.AuthenticationDTO
import org.rescado.server.controller.dto.res.CoordinatesDTO
import org.rescado.server.controller.dto.res.ImageDTO
import org.rescado.server.controller.dto.res.NewsDTO
import org.rescado.server.controller.dto.res.Response
import org.rescado.server.controller.dto.res.SessionDTO
import org.rescado.server.controller.dto.res.ShelterDTO
import org.rescado.server.persistence.entity.Account
import org.rescado.server.persistence.entity.Admin
import org.rescado.server.persistence.entity.Animal
import org.rescado.server.persistence.entity.Image
import org.rescado.server.persistence.entity.News
import org.rescado.server.persistence.entity.Session
import org.rescado.server.persistence.entity.Shelter
import org.springframework.data.domain.Page
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.util.UriComponentsBuilder
import java.time.Duration
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.servlet.http.HttpServletRequest

// region Response mappers

fun Response.build(httpStatusOverride: HttpStatus? = null) = ResponseEntity(
    this,
    this.httpHeaders,
    httpStatusOverride ?: this.httpStatus
)

fun List<Response>.build(httpStatusOverride: HttpStatus? = null) = ResponseEntity(
    this,
    this.firstOrNull()?.httpHeaders ?: HttpHeaders.EMPTY,
    httpStatusOverride ?: this.firstOrNull()?.httpStatus ?: HttpStatus.OK
)

fun List<Response>.withLinks(req: HttpServletRequest, result: Page<*>): List<Response> {
    if (isNotEmpty()) {
        val linkBuilder = StringBuilder()
        val urlBuilder = UriComponentsBuilder.fromHttpUrl("${req.requestURL}?${req.queryString ?: ""}")

        if (result.number != 0)
            linkBuilder.append("<${urlBuilder.replaceQueryParam("page", result.number - 1).toUriString()}>; rel=\"prev\", ")
        if (result.number != result.totalPages - 1)
            linkBuilder.append("<${urlBuilder.replaceQueryParam("page", result.number + 1).toUriString()}>; rel=\"next\", ")

        linkBuilder.append("<${urlBuilder.replaceQueryParam("page", 0).toUriString()}>; rel=\"first\", ")
        linkBuilder.append("<${urlBuilder.replaceQueryParam("page", result.totalPages - 1).toUriString()}>; rel=\"last\"")

        first().httpHeaders.add(HttpHeaders.LINK, linkBuilder.toString())
    }
    return this
}
// endregion
// region Point mappers

fun Point.toCoordinatesDTO() = CoordinatesDTO(
    latitude = y,
    longitude = x,
)
// endregion
// region Account mappers

fun Account.toNewAccountDTO(authorization: String) = AuthenticationDTO(
    httpStatus = HttpStatus.CREATED,
    authorization = authorization,
    status = status.name,
)

fun Account.toAuthenticationDTO(authorization: String) = AuthenticationDTO(
    authorization = authorization,
    status = status.name,
)

fun Account.toAccountDTO() = AccountDTO(
    id = id,
    status = status.name,
    uuid = uuid,
    email = email,
    name = name,
    avatar = avatar?.toImageDTO(),
    shelter = shelter?.toShelterDTO(true),
)

fun List<Account>.toAccountArrayDTO() = this.map { it.toAccountDTO() }
// endregion
// region Admin mappers

fun Admin.toAdminDTO() = AdminDTO(
    id = id,
    username = username,
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

fun Shelter.toShelterDTO(shortVersion: Boolean = false) =
    if (shortVersion)
        ShelterDTO(
            id = id,
            name = name,
            city = city,
            country = country,
            coordinates = geometry.toCoordinatesDTO(),
            logo = logo.toImageDTO(),
        )
    else
        ShelterDTO(
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
            logo = logo.toImageDTO(),
            banner = banner?.toImageDTO(),
        )

fun List<Shelter>.toShelterArrayDTO() = this.map { it.toShelterDTO() }
// endregion
// region Animal mappers

fun Animal.toAnimalDTO(now: ZonedDateTime? = null) =
    if (now == null)
        AnimalDTO(
            id = id,
            name = name,
            kind = kind.name,
            breed = breed,
            sex = sex.name,
            availability = availability.name,
            photos = photos.toImageArrayDTO(),
            shelter = shelter.toShelterDTO(true),
        )
    else
        AnimalDTO(
            id = id,
            name = name,
            description = description,
            kind = kind.name,
            breed = breed,
            sex = sex.name,
            age = (Duration.between(birthday.atStartOfDay(ZoneId.systemDefault()), now).toDays() / 365).toInt(),
            weight = weight,
            vaccinated = vaccinated,
            sterilized = sterilized,
            availability = availability.name,
            photos = photos.toImageArrayDTO(),
            shelter = shelter.toShelterDTO(true),
        )

fun List<Animal>.toAnimalArrayDTO(now: ZonedDateTime? = null) = this.map { it.toAnimalDTO(now) }
// endregion
// region Image mappers

fun Image.toImageDTO() = ImageDTO(
    id = if (type == Image.Type.PHOTO) id else null,
    reference = reference,
    type = type.name,
    source = source.name,
)

fun MutableSet<Image>.toImageArrayDTO() = this.map { it.toImageDTO() }
// endregion
// region News mappers

fun News.toNewsDTO() = NewsDTO(
    type = type.name,
    timestamp = timestamp,
    reference = animal.toAnimalDTO()
)

fun Collection<News>.toNewsArrayDTO() = this.map { it.toNewsDTO() }
// endregion
