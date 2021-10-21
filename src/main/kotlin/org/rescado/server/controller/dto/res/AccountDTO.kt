package org.rescado.server.controller.dto.res

data class AccountDTO(
    val id: Long,
    val status: String,
    val uuid: String,
    val email: String?,
    val name: String?,
    val avatar: ImageDTO?,
    val shelter: ShelterDTO?,
) : Response()
