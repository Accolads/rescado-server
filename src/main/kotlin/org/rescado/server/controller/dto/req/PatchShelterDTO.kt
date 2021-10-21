package org.rescado.server.controller.dto.req

import org.hibernate.validator.constraints.URL
import org.rescado.server.controller.dto.validation.InRange
import javax.validation.constraints.Email

data class PatchShelterDTO(

    val name: String?,

    @get:Email(message = "{Email.PatchShelterDTO.email}")
    val email: String?,

    @get:URL(message = "{URL.PatchShelterDTO.website}")
    val website: String?,

    @get:URL(message = "{URL.PatchShelterDTO.newsfeed}")
    val newsfeed: String?,

    val address: String?,

    val postalCode: String?,

    val city: String?,

    val country: String?,

    @get:InRange(message = "{InRange.PatchShelterDTO.latitude}", min = -90.0, max = 90.0)
    val latitude: Double?,

    @get:InRange(message = "{InRange.PatchShelterDTO.longitude}", min = -180.0, max = 180.0)
    val longitude: Double?,

    val logo: String?,

    val banner: String?,
)
