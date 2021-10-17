package org.rescado.server.controller.dto.res

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ShelterDTO(
    val id: Long,
    val name: String,
    val email: String? = null,
    val website: String? = null,
    val newsfeed: String? = null,
    val address: String? = null,
    val postalCode: String? = null,
    val city: String,
    val country: String,
    val coordinates: CoordinatesDTO,
    val logo: String,
    val banner: String? = null,
) : Response()
