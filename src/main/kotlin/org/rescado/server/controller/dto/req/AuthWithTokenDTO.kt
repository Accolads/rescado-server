package org.rescado.server.controller.dto.req

import com.fasterxml.jackson.annotation.JsonProperty
import org.rescado.server.controller.dto.validation.InRange
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Pattern

data class AuthWithTokenDTO(

    @get:NotEmpty(message = "{NotEmpty.AuthWithTokenDTO.uuid}")
    @get:Pattern(message = "{Pattern.AuthWithTokenDTO.uuid}", regexp = "^[\\w]{8}-[\\w]{4}-[\\w]{4}-[\\w]{4}-[\\w]{12}$")
    val uuid: String,

    @JsonProperty("token")
    @get:NotEmpty(message = "{NotEmpty.AuthWithTokenDTO.refreshToken}")
    @get:Pattern(message = "{Pattern.AuthWithTokenDTO.refreshToken}", regexp = "^[\\w]{8}-[\\w]{4}-[\\w]{4}-[\\w]{4}-[\\w]{12}$")
    val token: String,

    @get:InRange(min = -90f, max = 90f, message = "{InRange.AuthWithTokenDTO.latitude}")
    override val latitude: Float?,

    @get:InRange(min = -180f, max = 180f, message = "{InRange.AuthWithTokenDTO.longitude}")
    override val longitude: Float?,

) : AuthDTO(latitude, longitude)
