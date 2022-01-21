package org.rescado.server.controller.dto.res

import com.fasterxml.jackson.annotation.JsonInclude

class CardActionDTO(
    @get:JsonInclude(JsonInclude.Include.NON_EMPTY) val skipped: List<Long>? = null,
    @get:JsonInclude(JsonInclude.Include.NON_EMPTY) val liked: List<Long>? = null,
    @get:JsonInclude(JsonInclude.Include.NON_EMPTY) val followed: List<Long>? = null,
    errors: List<String>? = null,
) : Response(
    errors = errors,
)
