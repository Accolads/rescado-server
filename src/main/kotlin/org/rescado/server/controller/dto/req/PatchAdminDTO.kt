package org.rescado.server.controller.dto.req

import org.rescado.server.controller.dto.validation.WhitelistedPassword
import javax.validation.constraints.Size

data class PatchAdminDTO(

    val username: String?,

    @get:Size(message = "{Size.PatchAdminDTO.password}", min = 8)
    @get:WhitelistedPassword(message = "{WhitelistedPassword.PatchAdminDTO.password}")
    val password: String?,
)
