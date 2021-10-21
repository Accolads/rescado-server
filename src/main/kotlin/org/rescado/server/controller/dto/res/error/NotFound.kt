package org.rescado.server.controller.dto.res.error

import org.rescado.server.controller.dto.res.Response
import org.springframework.http.HttpStatus

class NotFound(errors: List<String>) : Response(
    httpStatus = HttpStatus.NOT_FOUND,
    errors = errors,
) {
    constructor(error: String) : this(listOf(error))
}
