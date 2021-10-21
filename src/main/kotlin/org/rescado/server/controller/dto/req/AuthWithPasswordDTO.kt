package org.rescado.server.controller.dto.req

import com.fasterxml.jackson.annotation.JsonProperty
import org.rescado.server.controller.dto.validation.InRange
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class AuthWithPasswordDTO(

    @JsonProperty("email")
    @get:NotBlank(message = "{NotBlank.AuthWithPasswordDTO.email}")
    @get:Email(message = "{Email.AuthWithPasswordDTO.email}")
    val email: String?,

    @JsonProperty("password")
    @get:NotBlank(message = "{NotBlank.AuthWithPasswordDTO.password}")
    val password: String?,

    @JsonProperty("latitude")
    @get:InRange(message = "{InRange.AuthWithPasswordDTO.latitude}", min = -90.0, max = 90.0)
    override val latitude: Double?,

    @JsonProperty("longitude")
    @get:InRange(message = "{InRange.AuthWithPasswordDTO.longitude}", min = -180.0, max = 180.0)
    override val longitude: Double?,

) : AuthDTO(latitude, longitude)
