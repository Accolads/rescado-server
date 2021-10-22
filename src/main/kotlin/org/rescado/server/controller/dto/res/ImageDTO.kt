package org.rescado.server.controller.dto.res

import com.fasterxml.jackson.annotation.JsonInclude

data class ImageDTO(
    @get:JsonInclude(JsonInclude.Include.NON_NULL) val id: Long? = null,
    val reference: String,
    val type: String,
    val source: String,
) : Response()
