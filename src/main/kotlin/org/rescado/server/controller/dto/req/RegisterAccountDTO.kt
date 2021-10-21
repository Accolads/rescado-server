package org.rescado.server.controller.dto.req

import org.rescado.server.controller.dto.validation.InRange

data class RegisterAccountDTO(

    @get:InRange(message = "{InRange.RegisterAccountDTO.latitude}", min = -90.0, max = 90.0)
    override val latitude: Double?,

    @get:InRange(message = "{InRange.RegisterAccountDTO.longitude}", min = -180.0, max = 180.0)
    override val longitude: Double?,

) : AuthDTO(latitude, longitude)
