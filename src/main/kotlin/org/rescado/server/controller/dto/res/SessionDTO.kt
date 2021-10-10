package org.rescado.server.controller.dto.res

import java.time.ZonedDateTime

data class SessionDTO(
    val refreshToken: String,
    val agent: String,
    val firstLogin: ZonedDateTime,
    val lastLogin: ZonedDateTime,
    val ipAddress: String,
    val coordinates: CoordinatesDTO?,
) : Response()
