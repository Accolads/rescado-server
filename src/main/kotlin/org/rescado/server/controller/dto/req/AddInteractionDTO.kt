package org.rescado.server.controller.dto.req

import javax.validation.constraints.NotEmpty

data class AddInteractionDTO(

    @get:NotEmpty(message = "{NotEmpty.AddInteractionDTO.ids}")
    val ids: List<Long>
)
