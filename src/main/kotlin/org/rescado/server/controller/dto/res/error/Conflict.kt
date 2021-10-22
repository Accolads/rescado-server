package org.rescado.server.controller.dto.res.error

import org.rescado.server.controller.dto.res.Response
import org.springframework.http.HttpStatus

class Conflict(errors: List<String>) : Response(
    httpStatus = HttpStatus.CONFLICT,
    errors = errors,
) {
    constructor(error: String) : this(listOf(error))
}
