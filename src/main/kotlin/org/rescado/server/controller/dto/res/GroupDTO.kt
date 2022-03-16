package org.rescado.server.controller.dto.res

data class GroupDTO(
    val id: Long,
    val status: String,
    val members: List<MembershipDTO>,
) : Response()
