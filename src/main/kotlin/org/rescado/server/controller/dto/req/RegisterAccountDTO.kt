package org.rescado.server.controller.dto.req

import org.rescado.server.controller.dto.validation.InRange

data class RegisterAccountDTO(

    @get:InRange(min = -90f, max = 90f, message = "{InRange.RegisterAccountDTO.latitude}")
    override val latitude: Float?,

    @get:InRange(min = -180f, max = 180f, message = "{InRange.RegisterAccountDTO.longitude}")
    override val longitude: Float?,

) : AuthDTO(latitude, longitude)
