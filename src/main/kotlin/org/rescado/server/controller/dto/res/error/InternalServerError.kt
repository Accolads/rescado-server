package org.rescado.server.controller.dto.res.error

import org.rescado.server.controller.dto.res.Response
import org.springframework.http.HttpStatus

data class InternalServerError(
    val errors: List<String>
) : Response(httpStatus = HttpStatus.INTERNAL_SERVER_ERROR) {
    constructor(error: String) : this(listOf(error))
}
