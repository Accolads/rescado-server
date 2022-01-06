package org.rescado.server.controller.dto.res.error

import org.rescado.server.controller.dto.res.Response
import org.springframework.http.HttpStatus

class PayloadTooLarge(errors: List<String>) : Response(
    httpStatus = HttpStatus.PAYLOAD_TOO_LARGE,
    errors = errors,
) {
    constructor(error: String) : this(listOf(error))
}
