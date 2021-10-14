package org.rescado.server.controller.dto.req

import org.hibernate.validator.constraints.URL
import org.rescado.server.controller.dto.validation.InRange
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class AddShelterDTO(

    @get:NotBlank(message = "{NotBlank.AddShelterDTO.name}")
    val name: String,

    @get:NotBlank(message = "{NotBlank.AddShelterDTO.email}")
    @get:Email(message = "{Email.AddShelterDTO.email}")
    val email: String,

    @get:NotBlank(message = "{NotBlank.AddShelterDTO.website}")
    @get:URL(message = "{URL.AddShelterDTO.website}")
    val website: String,

    @get:URL(message = "{URL.AddShelterDTO.newsfeed}")
    val newsfeed: String?,

    @get:NotBlank(message = "{NotBlank.AddShelterDTO.address}")
    val address: String,

    @get:NotBlank(message = "{NotBlank.AddShelterDTO.postalCode}")
    val postalCode: String,

    @get:NotBlank(message = "{NotBlank.AddShelterDTO.city}")
    val city: String,

    @get:NotBlank(message = "{NotBlank.AddShelterDTO.country}")
    val country: String,

    @get:NotNull(message = "{NotNull.AddShelterDTO.latitude}")
    @get:InRange(min = -90.0, max = 90.0, message = "{InRange.AddShelterDTO.latitude}")
    val latitude: Double,

    @get:NotNull(message = "{NotNull.AddShelterDTO.longitude}")
    @get:InRange(min = -180.0, max = 180.0, message = "{InRange.AddShelterDTO.longitude}")
    val longitude: Double,

    @get:NotBlank(message = "{NotBlank.AddShelterDTO.logo}")
    val logo: String,

    val banner: String?,
)
