package org.rescado.server.controller.dto.res

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ShelterDTO(
    val id: Long,
    val name: String,
    val email: String?,
    val website: String?,
    val newsfeed: String?,
    val address: String?,
    val postalCode: String?,
    val city: String,
    val country: String,
    val coordinates: CoordinatesDTO,
    val logo: String,
    val banner: String?,
) : Response()
