package org.rescado.server.controller.dto.res

data class MembershipDTO(
    val uuid: String,
    val name: String,
    val status: String,
    val avatar: ImageDTO?,
) : Response()
