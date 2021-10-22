package org.rescado.server.controller.dto.req

import com.fasterxml.jackson.annotation.JsonProperty
import org.rescado.server.controller.dto.validation.InRange

data class RegisterAccountDTO(

    @JsonProperty("latitude")
    @get:InRange(message = "{InRange.RegisterAccountDTO.latitude}", min = -90.0, max = 90.0)
    override val latitude: Double?,

    @JsonProperty("longitude")
    @get:InRange(message = "{InRange.RegisterAccountDTO.longitude}", min = -180.0, max = 180.0)
    override val longitude: Double?,

) : AuthDTO(latitude, longitude)
