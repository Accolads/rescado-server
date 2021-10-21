package org.rescado.server.controller.dto.req

import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.validator.constraints.URL
import org.rescado.server.controller.dto.validation.InRange
import javax.validation.constraints.Email

data class PatchShelterDTO(

    @JsonProperty("name")
    val name: String?,

    @JsonProperty("email")
    @get:Email(message = "{Email.PatchShelterDTO.email}")
    val email: String?,

    @JsonProperty("website")
    @get:URL(message = "{URL.PatchShelterDTO.website}")
    val website: String?,

    @JsonProperty("newsfeed")
    @get:URL(message = "{URL.PatchShelterDTO.newsfeed}")
    val newsfeed: String?,

    @JsonProperty("address")
    val address: String?,

    @JsonProperty("postalCode")
    val postalCode: String?,

    @JsonProperty("city")
    val city: String?,

    @JsonProperty("country")
    val country: String?,

    @JsonProperty("latitude")
    @get:InRange(message = "{InRange.PatchShelterDTO.latitude}", min = -90.0, max = 90.0)
    val latitude: Double?,

    @JsonProperty("longitude")
    @get:InRange(message = "{InRange.PatchShelterDTO.longitude}", min = -180.0, max = 180.0)
    val longitude: Double?,

    @JsonProperty("logo")
    val logo: String?,

    @JsonProperty("banner")
    val banner: String?,
)
