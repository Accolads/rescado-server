package org.rescado.server.controller.dto.res

import com.fasterxml.jackson.annotation.JsonInclude

data class AccountDTO(
    val id: Long,
    val status: String,
    val uuid: String,
    val name: String?,
    val appleLinked: Boolean = false,
    val googleLinked: Boolean = false,
    val facebookLinked: Boolean = false,
    val twitterLinked: Boolean = false,
    val email: String?,
    val avatar: ImageDTO?,
    val groups: List<GroupDTO>,
    @get:JsonInclude(JsonInclude.Include.NON_NULL) val shelter: ShelterDTO?,
) : Response()
