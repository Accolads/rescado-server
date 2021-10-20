package org.rescado.server.controller.dto.req

import javax.validation.constraints.NotBlank

data class AddAnimalPhotoDTO(

    @get:NotBlank(message = "{NotBlank.AddAnimalPhotoDTO.reference}")
    val reference: String?,
)
