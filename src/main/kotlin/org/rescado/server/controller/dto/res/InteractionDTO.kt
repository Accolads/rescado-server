package org.rescado.server.controller.dto.res

import com.fasterxml.jackson.annotation.JsonInclude

class InteractionDTO(
    @get:JsonInclude(JsonInclude.Include.NON_EMPTY) val likes: List<Long>? = null,
    @get:JsonInclude(JsonInclude.Include.NON_EMPTY) val following: List<Long>? = null,
    errors: List<String>,
) : Response(
    errors = errors,
)
