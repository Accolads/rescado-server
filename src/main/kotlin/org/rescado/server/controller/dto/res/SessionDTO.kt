package org.rescado.server.controller.dto.res

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.ZonedDateTime

data class SessionDTO(
    val sessions: List<SessionDTOsession>

) : Response()

data class SessionDTOsession(

    @JsonProperty("refresh_token") val token: String,
    val description: String,
    @JsonProperty("first_login") val firstLogin: ZonedDateTime,
    @JsonProperty("last_login") val lastLogin: ZonedDateTime
)
