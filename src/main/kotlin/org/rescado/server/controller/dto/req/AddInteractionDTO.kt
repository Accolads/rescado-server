package org.rescado.server.controller.dto.req

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotEmpty

data class AddInteractionDTO(

    @JsonProperty("ids")
    @get:NotEmpty(message = "{NotEmpty.AddInteractionDTO.ids}")
    val ids: List<Long>?
)
