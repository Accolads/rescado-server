package org.rescado.server.controller.dto.res

data class AccountDTO(
    val status: String,
    val uuid: String,
    val email: String?,
    val name: String?,
    val shelter: ShelterDTO?,
) : Response()
