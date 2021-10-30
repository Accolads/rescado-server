package org.rescado.server.controller.dto.req

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotEmpty

data class SetOfIdsDTO(

    @JsonProperty("ids")
    @get:NotEmpty(message = "{NotEmpty.SetOfIdsDTO.ids}")
    val ids: Set<Long>?
)
