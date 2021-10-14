package org.rescado.server.controller.dto.res.error

import org.rescado.server.controller.dto.res.Response
import org.springframework.http.HttpStatus

class InternalServerError(errors: List<String>) : Response(
    httpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
    errors = errors,
) {
    constructor(error: String) : this(listOf(error))
}
