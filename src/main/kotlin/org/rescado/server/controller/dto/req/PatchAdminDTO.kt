package org.rescado.server.controller.dto.req

import com.fasterxml.jackson.annotation.JsonProperty
import org.rescado.server.controller.dto.validation.WhitelistedPassword
import javax.validation.constraints.Size

data class PatchAdminDTO(

    @JsonProperty("username")
    val username: String?,

    @JsonProperty("password")
    @get:Size(message = "{Size.PatchAdminDTO.password}", min = 8)
    @get:WhitelistedPassword(message = "{WhitelistedPassword.PatchAdminDTO.password}")
    val password: String?,
)
