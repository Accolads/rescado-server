package org.rescado.server.controller.dto.res

import java.time.Instant
import java.time.ZonedDateTime

data class InfoDTO(
    val name: String,
    val version: String,
    val build: Instant,
    val clock: ZonedDateTime,
    val sessions: Int,
) : Response()
