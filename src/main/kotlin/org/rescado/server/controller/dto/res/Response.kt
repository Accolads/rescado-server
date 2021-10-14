package org.rescado.server.controller.dto.res

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus

open class Response(
    @get:JsonIgnore open val httpStatus: HttpStatus = HttpStatus.OK, // default response HTTP status
    @get:JsonIgnore open val httpHeaders: HttpHeaders = HttpHeaders.EMPTY, // default response HTTP headers
    @get:JsonInclude(JsonInclude.Include.NON_EMPTY) open val errors: List<String> = listOf(), // error descriptions for 4xx/5xx codes
)
