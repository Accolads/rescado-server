package org.rescado.server.controller.dto.res.error

import com.fasterxml.jackson.annotation.JsonIgnore
import org.rescado.server.controller.dto.res.Response
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus

data class Unauthorized(
    @JsonIgnore val reason: Reason,
    @JsonIgnore val realm: String,
    var error: String = "This resource requires valid authentication credentials"
) : Response(
    httpStatus = HttpStatus.UNAUTHORIZED,
    httpHeaders = HttpHeaders()
) {
    init {
        var updatedError = true

        when (reason) {
            Reason.INVALID_ACCESS_TOKEN -> this.error = "The provided access token is invalid"
            Reason.INVALID_TOKEN_ACCOUNT -> this.error = "The provided access token's account does not exist"
            Reason.INVALID_REFRESH_TOKEN -> this.error = "The provided refresh token is invalid"
            Reason.EXPIRED_ACCESS_TOKEN -> this.error = "The provided access token has expired"
            Reason.EXPIRED_REFRESH_TOKEN -> this.error = "The provided refresh token has expired"
            else -> updatedError = false
        }

        httpHeaders.add(
            HttpHeaders.WWW_AUTHENTICATE,
            if (updatedError) """Bearer realm="$realm", charset="UTF-8", error="invalid_token", error_description="$error""""
            else """Bearer realm="$realm", charset="UTF-8""""
        )
    }

    enum class Reason {
        NO_TOKEN_PROVIDED,
        INVALID_TOKEN_ACCOUNT,
        INVALID_ACCESS_TOKEN,
        INVALID_REFRESH_TOKEN,
        EXPIRED_ACCESS_TOKEN,
        EXPIRED_REFRESH_TOKEN
    }
}
