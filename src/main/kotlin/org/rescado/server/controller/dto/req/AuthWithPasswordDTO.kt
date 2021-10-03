package org.rescado.server.controller.dto.req

import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

data class AuthWithPasswordDTO(
    @get:NotEmpty(message = "{NotEmpty.AuthWithPasswordDTO.email}")
    @get:Email(message = "{Email.AuthWithPasswordDTO.email}")
    val email: String,

    @get:NotEmpty(message = "{NotEmpty.AuthWithPasswordDTO.password}")
    val password: String,

    val client: String?
)
