package org.rescado.server.controller.dto.res

data class ImageDTO(
    val reference: String,
    val type: String,
    val source: String,
) : Response()
