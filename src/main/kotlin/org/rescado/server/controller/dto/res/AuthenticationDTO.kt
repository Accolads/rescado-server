package org.rescado.server.controller.dto.res

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus

data class AuthenticationDTO(
    @JsonIgnore override val httpStatus: HttpStatus = HttpStatus.OK,
    @JsonIgnore override val httpHeaders: HttpHeaders = HttpHeaders(),

    @JsonIgnore val authorization: String,

    val status: String,
    val uuid: String,
    val email: String?,
    val shelter: ShelterDTO?,
) : Response() {
    init {
        httpHeaders.add(HttpHeaders.AUTHORIZATION, this.authorization)
    }
}
