package org.rescado.server.controller.dto.req

import com.fasterxml.jackson.annotation.JsonProperty
import org.rescado.server.controller.dto.validation.InRange
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

data class AuthWithTokenDTO(

    @JsonProperty("uuid")
    @get:NotBlank(message = "{NotBlank.AuthWithTokenDTO.uuid}")
    @get:Pattern(message = "{Pattern.AuthWithTokenDTO.uuid}", regexp = "^[\\w]{8}-[\\w]{4}-[\\w]{4}-[\\w]{4}-[\\w]{12}$")
    val uuid: String?,

    @JsonProperty("token")
    @get:NotBlank(message = "{NotBlank.AuthWithTokenDTO.token}")
    @get:Pattern(message = "{Pattern.AuthWithTokenDTO.token}", regexp = "^[\\w]{8}-[\\w]{4}-[\\w]{4}-[\\w]{4}-[\\w]{12}$")
    val token: String?,

    @JsonProperty("latitude")
    @get:InRange(message = "{InRange.AuthWithTokenDTO.latitude}", min = -90.0, max = 90.0)
    override val latitude: Double?,

    @JsonProperty("longitude")
    @get:InRange(message = "{InRange.AuthWithTokenDTO.longitude}", min = -180.0, max = 180.0)
    override val longitude: Double?,

) : AuthDTO(latitude, longitude)
