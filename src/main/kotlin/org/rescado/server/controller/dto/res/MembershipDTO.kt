package org.rescado.server.controller.dto.res

data class MembershipDTO(
    val name: String,
    val avatar: ImageDTO?,
) : Response()
