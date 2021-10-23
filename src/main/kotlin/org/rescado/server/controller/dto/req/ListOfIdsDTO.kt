package org.rescado.server.controller.dto.req

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotEmpty

data class ListOfIdsDTO(

    @JsonProperty("ids")
    @get:NotEmpty(message = "{NotEmpty.ListOfIdsDTO.ids}")
    val ids: List<Long>?
)
