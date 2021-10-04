package org.rescado.server.controller.dto.req

import org.rescado.server.controller.dto.validation.WhitelistedPassword
import javax.validation.constraints.Email
import javax.validation.constraints.Size

data class UpdateAccountDTO(

    @get:Email(message = "{Email.UpdateAccountEmailDTO.email}")
    val email: String?,

    @get:Size(min = 8, message = "{Size.UpdatePasswordDTO.password}")
    @get:WhitelistedPassword(message = "{WhitelistedPassword.UpdatePasswordDTO.password}")
    val password: String?,

    val name: String?,
)
