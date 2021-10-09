package org.rescado.server.controller.dto.req

import org.rescado.server.controller.dto.validation.InRange
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

data class AuthWithPasswordDTO(

    @get:NotEmpty(message = "{NotEmpty.AuthWithPasswordDTO.email}")
    @get:Email(message = "{Email.AuthWithPasswordDTO.email}")
    val email: String,

    @get:NotEmpty(message = "{NotEmpty.AuthWithPasswordDTO.password}")
    val password: String,

    @get:InRange(min = -90.0, max = 90.0, message = "{InRange.AuthWithPasswordDTO.latitude}")
    override val latitude: Double?,
    @get:InRange(min = -180.0, max = 180.0, message = "{InRange.AuthWithPasswordDTO.longitude}")
    override val longitude: Double?,

) : AuthDTO(latitude, longitude)
