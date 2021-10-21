package org.rescado.server.controller.dto.res.error

import org.rescado.server.controller.dto.res.Response
import org.springframework.http.HttpStatus

class MethodNotAllowed(errors: List<String>) : Response(
    httpStatus = HttpStatus.METHOD_NOT_ALLOWED,
    errors = errors,
) {
    constructor(error: String) : this(listOf(error))
}
