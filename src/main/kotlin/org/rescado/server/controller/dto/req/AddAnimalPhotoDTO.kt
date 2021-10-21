package org.rescado.server.controller.dto.req

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotBlank

data class AddAnimalPhotoDTO(

    @JsonProperty("reference")
    @get:NotBlank(message = "{NotBlank.AddAnimalPhotoDTO.reference}")
    val reference: String?,
)
