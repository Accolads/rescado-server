package org.rescado.server.controller.dto.req

import org.rescado.server.controller.dto.validation.InRange

data class RegisterAccountDTO(

    @get:InRange(min = -90.0, max = 90.0, message = "{InRange.RegisterAccountDTO.latitude}")
    override val latitude: Double?,

    @get:InRange(min = -180.0, max = 180.0, message = "{InRange.RegisterAccountDTO.longitude}")
    override val longitude: Double?,

) : AuthDTO(latitude, longitude)
