package org.rescado.server.controller.dto.res

import com.fasterxml.jackson.annotation.JsonInclude

data class AccountDTO(
    val id: Long,
    val status: String,
    val uuid: String,
    val email: String?,
    val name: String?,
    val avatar: ImageDTO?,
    @get:JsonInclude(JsonInclude.Include.NON_NULL) val shelter: ShelterDTO?,
) : Response()
