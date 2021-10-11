package org.rescado.server.controller.dto.res.error

import com.fasterxml.jackson.annotation.JsonIgnore
import org.rescado.server.controller.dto.res.Response
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import java.util.Locale

data class Unauthorized(
    @JsonIgnore val reason: Reason,
    @JsonIgnore val realm: String,
) : Response(
    httpStatus = HttpStatus.UNAUTHORIZED,
    httpHeaders = HttpHeaders()
) {
    init {
        val description = when (reason) {
            Reason.INVALID_ACCESS_TOKEN -> "The provided access token is invalid"
            Reason.INVALID_TOKEN_ACCOUNT -> "The provided access token's account does not exist"
            Reason.INVALID_REFRESH_TOKEN -> "The provided refresh token is invalid"
            Reason.EXPIRED_ACCESS_TOKEN -> "The provided access token has expired"
            Reason.EXPIRED_REFRESH_TOKEN -> "The provided refresh token has expired"
            else -> "This resource requires valid authentication credentials"
        }

        httpHeaders.add(
            HttpHeaders.WWW_AUTHENTICATE,
            """Bearer realm="$realm", charset="UTF-8", error="${reason.name.lowercase(Locale.ENGLISH)}", error_description="$description""""
        )
    }

    enum class Reason {
        NO_TOKEN_PROVIDED,
        NO_CREDENTIALS_PROVIDED,

        INVALID_ACCESS_TOKEN,
        INVALID_TOKEN_ACCOUNT,
        INVALID_REFRESH_TOKEN,
        EXPIRED_ACCESS_TOKEN,
        EXPIRED_REFRESH_TOKEN,
    }
}
