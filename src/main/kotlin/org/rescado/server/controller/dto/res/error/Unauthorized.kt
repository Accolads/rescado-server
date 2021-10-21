package org.rescado.server.controller.dto.res.error

import com.fasterxml.jackson.annotation.JsonIgnore
import org.rescado.server.controller.dto.res.Response
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import java.util.Locale

class Unauthorized(
    @get:JsonIgnore val error: String,
    @get:JsonIgnore val reason: Reason,
    @get:JsonIgnore val realm: String,
) : Response(
    httpStatus = HttpStatus.UNAUTHORIZED,
    errors = listOf(error),
) {
    init {
        val description = when (reason) {
            Reason.INVALID_CREDENTIALS -> "The provided authentication string is of the wrong type"
            Reason.MALFORMED_CREDENTIALS -> "The provided authentication string is invalid"

            Reason.INVALID_ACCESS_TOKEN -> "The access token's signature is invalid"
            Reason.EXPIRED_ACCESS_TOKEN -> "The access token has expired"
            Reason.EXPIRED_REFRESH_TOKEN -> "The refresh token has expired"
            else -> "No authentication credentials were provided"
        }

        httpHeaders.add(
            HttpHeaders.WWW_AUTHENTICATE,
            """Bearer realm="$realm", charset="UTF-8", error="${reason.name.lowercase(Locale.ENGLISH)}", error_description="$description""""
        )
    }

    enum class Reason {
        NO_CREDENTIALS,
        INVALID_CREDENTIALS,
        MALFORMED_CREDENTIALS,

        INVALID_ACCESS_TOKEN,
        EXPIRED_ACCESS_TOKEN,
        EXPIRED_REFRESH_TOKEN,
    }
}
