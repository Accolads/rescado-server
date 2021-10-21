package org.rescado.server.controller.dto.req

import org.rescado.server.controller.dto.validation.InRange
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

data class AuthAnonymouslyDTO(

    @get:NotBlank(message = "{NotBlank.AuthAnonymouslyDTO.password}")
    @get:Pattern(message = "{Pattern.AuthAnonymouslyDTO.uuid}", regexp = "^[\\w]{8}-[\\w]{4}-[\\w]{4}-[\\w]{4}-[\\w]{12}$")
    val uuid: String?,

    @get:InRange(message = "{InRange.AuthAnonymouslyDTO.latitude}", min = -90.0, max = 90.0)
    override val latitude: Double?,

    @get:InRange(message = "{InRange.AuthAnonymouslyDTO.longitude}", min = -180.0, max = 180.0)
    override val longitude: Double?,

) : AuthDTO(latitude, longitude)
