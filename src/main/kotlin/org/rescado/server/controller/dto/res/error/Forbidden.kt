package org.rescado.server.controller.dto.res.error

import org.rescado.server.controller.dto.res.Response
import org.springframework.http.HttpStatus

class Forbidden(errors: List<String>) : Response(
    httpStatus = HttpStatus.FORBIDDEN,
    errors = errors,
) {
    constructor(error: String) : this(listOf(error))
}
