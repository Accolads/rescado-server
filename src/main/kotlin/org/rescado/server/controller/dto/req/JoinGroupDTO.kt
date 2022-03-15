package org.rescado.server.controller.dto.req

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

data class JoinGroupDTO(

    @JsonProperty("uuid")
    @get:NotBlank(message = "{NotBlank.JoinGroupDTO.uuid}")
    @get:Pattern(message = "{Pattern.JoinGroupDTO.uuid}", regexp = "^[\\w]{8}-[\\w]{4}-[\\w]{4}-[\\w]{4}-[\\w]{12}$")
    val uuid: String?,
)
