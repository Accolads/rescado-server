package org.rescado.server.controller.dto.req

import com.fasterxml.jackson.annotation.JsonProperty
import org.rescado.server.controller.dto.validation.WhitelistedPassword
import javax.validation.constraints.Email
import javax.validation.constraints.Size

data class PatchAccountDTO(

    @JsonProperty("name")
    val name: String?,

    @JsonProperty("email")
    @get:Email(message = "{Email.PatchAccountDTO.email}")
    val email: String?,

    @JsonProperty("password")
    @get:Size(message = "{Size.PatchAccountDTO.password}", min = 8)
    @get:WhitelistedPassword(message = "{WhitelistedPassword.PatchAccountDTO.password}")
    val password: String?,

    @JsonProperty("avatar")
    val avatar: String?,
)
