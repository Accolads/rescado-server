package org.rescado.server.controller.dto.res.error

import org.rescado.server.controller.dto.res.Response
import org.springframework.http.HttpStatus

class BadRequest(errors: List<String>) : Response(
    httpStatus = HttpStatus.BAD_REQUEST,
    errors = errors,
) {
    constructor(error: String) : this(listOf(error))
}
