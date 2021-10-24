package org.rescado.server.controller.dto.res

import java.time.ZonedDateTime

data class NewsDTO(
    val type: String,
    val timestamp: ZonedDateTime,
    val reference: Response,
) : Response()
