package org.rescado.server.controller.dto.req

import org.rescado.server.controller.dto.validation.InRange
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class AuthWithPasswordDTO(

    @get:NotBlank(message = "{NotBlank.AuthWithPasswordDTO.email}")
    @get:Email(message = "{Email.AuthWithPasswordDTO.email}")
    val email: String?,

    @get:NotBlank(message = "{NotBlank.AuthWithPasswordDTO.password}")
    val password: String?,

    @get:InRange(min = -90.0, max = 90.0, message = "{InRange.AuthWithPasswordDTO.latitude}")
    override val latitude: Double?,

    @get:InRange(min = -180.0, max = 180.0, message = "{InRange.AuthWithPasswordDTO.longitude}")
    override val longitude: Double?,

) : AuthDTO(latitude, longitude)
