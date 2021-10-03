package org.rescado.server.controller.dto.res

data class AccountDTO(
    val uuid: String,
    val email: String?,
    val name: String?,
    val status: String,
) : Response()
