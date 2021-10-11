package org.rescado.server.controller.dto.res.error

import org.rescado.server.controller.dto.res.Response
import org.springframework.http.HttpStatus

data class Forbidden(
    val error: String = "The provided authentication does not suffice"
) : Response(httpStatus = HttpStatus.FORBIDDEN)
