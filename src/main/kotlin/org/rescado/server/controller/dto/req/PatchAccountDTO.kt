package org.rescado.server.controller.dto.req

import org.rescado.server.controller.dto.validation.WhitelistedPassword
import javax.validation.constraints.Email
import javax.validation.constraints.Size

data class PatchAccountDTO(

    val name: String?,

    @get:Email(message = "{Email.PatchAccountDTO.email}")
    val email: String?,

    @get:Size(message = "{Size.PatchAccountDTO.password}", min = 8)
    @get:WhitelistedPassword(message = "{WhitelistedPassword.PatchAccountDTO.password}")
    val password: String?,

    val avatar: String?,
)
