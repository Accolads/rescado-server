package org.rescado.server.controller.dto.req

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotNull

data class AddVolunteerDTO(

    @JsonProperty("accountId")
    @get:NotNull(message = "{NotNull.AddVolunteerDTO.accountId}")
    val accountId: Long?,

    @JsonProperty("shelterId")
    @get:NotNull(message = "{NotNull.AddVolunteerDTO.shelterId}")
    val shelterId: Long?,
)
