package org.rescado.server.controller.dto.req

import org.rescado.server.controller.dto.validation.WhitelistedPassword
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class AddAdminDTO(

    @get:NotBlank(message = "{NotBlank.AddAdminDTO.username}")
    val username: String?,

    @get:NotBlank(message = "{NotBlank.AddAdminDTO.password}")
    @get:Size(message = "{Size.AddAdminDTO.password}", min = 8)
    @get:WhitelistedPassword(message = "{WhitelistedPassword.AddAdminDTO.password}")
    val password: String?,
)
