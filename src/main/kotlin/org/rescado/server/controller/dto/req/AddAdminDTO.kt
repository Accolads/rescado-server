package org.rescado.server.controller.dto.req

import javax.validation.constraints.NotBlank

data class AddAdminDTO(

    @get:NotBlank(message = "{NotBlank.AddAdminDTO.email}")
    val username: String,

    @get:NotBlank(message = "{NotBlank.AddAdminDTO.password}")
    val password: String,
)
