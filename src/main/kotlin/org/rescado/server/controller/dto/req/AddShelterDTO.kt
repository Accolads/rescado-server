package org.rescado.server.controller.dto.req

import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.validator.constraints.URL
import org.rescado.server.controller.dto.validation.InRange
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class AddShelterDTO(

    @JsonProperty("name")
    @get:NotBlank(message = "{NotBlank.AddShelterDTO.name}")
    val name: String?,

    @JsonProperty("email")
    @get:NotBlank(message = "{NotBlank.AddShelterDTO.email}")
    @get:Email(message = "{Email.AddShelterDTO.email}")
    val email: String?,

    @JsonProperty("website")
    @get:NotBlank(message = "{NotBlank.AddShelterDTO.website}")
    @get:URL(message = "{URL.AddShelterDTO.website}")
    val website: String?,

    @JsonProperty("newsfeed")
    @get:URL(message = "{URL.AddShelterDTO.newsfeed}")
    val newsfeed: String?,

    @JsonProperty("address")
    @get:NotBlank(message = "{NotBlank.AddShelterDTO.address}")
    val address: String?,

    @JsonProperty("postalCode")
    @get:NotBlank(message = "{NotBlank.AddShelterDTO.postalCode}")
    val postalCode: String?,

    @JsonProperty("city")
    @get:NotBlank(message = "{NotBlank.AddShelterDTO.city}")
    val city: String?,

    @JsonProperty("country")
    @get:NotBlank(message = "{NotBlank.AddShelterDTO.country}")
    val country: String?,

    @JsonProperty("latitude")
    @get:NotNull(message = "{NotNull.AddShelterDTO.latitude}")
    @get:InRange(message = "{InRange.AddShelterDTO.latitude}", min = -90.0, max = 90.0)
    val latitude: Double?,

    @JsonProperty("longitude")
    @get:NotNull(message = "{NotNull.AddShelterDTO.longitude}")
    @get:InRange(message = "{InRange.AddShelterDTO.longitude}", min = -180.0, max = 180.0)
    val longitude: Double?,

    @JsonProperty("logo")
    @get:NotBlank(message = "{NotBlank.AddShelterDTO.logo}")
    val logo: String?,

    @JsonProperty("banner")
    val banner: String?,
)
