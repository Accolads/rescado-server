package org.rescado.server.controller.dto.req

import com.fasterxml.jackson.annotation.JsonProperty
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

    val client: String?,
)
