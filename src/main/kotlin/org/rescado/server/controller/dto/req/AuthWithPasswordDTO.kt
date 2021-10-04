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

    @get:InRange(min = -90f, max = 90f, message = "{InRange.AuthWithPasswordDTO.latitude}")
    override val latitude: Float?,
    @get:InRange(min = -180f, max = 180f, message = "{InRange.AuthWithPasswordDTO.longitude}")
    override val longitude: Float?,

) : AuthDTO(latitude, longitude)
