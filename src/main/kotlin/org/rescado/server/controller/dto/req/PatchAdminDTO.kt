package org.rescado.server.controller.dto.req

import org.rescado.server.controller.dto.validation.WhitelistedPassword
import javax.validation.constraints.Size

data class PatchAdminDTO(

    val username: String,

    @get:Size(min = 8, message = "{Size.PatchAdminDTO.password}")
    @get:WhitelistedPassword(message = "{WhitelistedPassword.PatchAdminDTO.password}")
    val password: String,
)
