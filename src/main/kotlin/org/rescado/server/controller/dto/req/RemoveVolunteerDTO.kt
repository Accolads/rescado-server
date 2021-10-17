package org.rescado.server.controller.dto.req

import javax.validation.constraints.NotNull

data class RemoveVolunteerDTO(

    @get:NotNull(message = "{NotNull.AddVolunteerDTO.accountId}")
    val accountId: Long,
)
