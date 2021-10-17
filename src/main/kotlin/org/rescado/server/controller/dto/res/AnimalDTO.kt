package org.rescado.server.controller.dto.res

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AnimalDTO(
    val id: Long,
    val name: String,
    val description: String? = null,
    val kind: String,
    val breed: String,
    val sex: String,
    val age: Int? = null,
    val weight: Int? = null,
    val vaccinated: Boolean? = null,
    val sterilized: Boolean? = null,
    val photos: List<String>,
    val shelter: ShelterDTO,
) : Response()
