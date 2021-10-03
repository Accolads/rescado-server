package org.rescado.server.controller.dto.res.error

import org.rescado.server.controller.dto.res.Response
import org.springframework.http.HttpStatus

data class NotFound(
    val error: String = "The data you are looking for could not be found"
) : Response(httpStatus = HttpStatus.NOT_FOUND)
