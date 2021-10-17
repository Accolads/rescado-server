package org.rescado.server.controller.dto.req

import javax.validation.constraints.NotNull

data class AddVolunteerDTO(

    @get:NotNull(message = "{NotNull.AddVolunteerDTO.accountId}")
    val accountId: Long,

    @get:NotNull(message = "{NotNull.AddVolunteerDTO.shelterId}")
    val shelterId: Long,
)
