package org.rescado.server.controller.dto.res

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDate

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AnimalDTO(
    val id: Long,
    val name: String,
    val description: String? = null,
    val kind: String,
    val breed: String,
    val sex: String,
    val birthday: LocalDate? = null,
    val weight: Int? = null,
    val vaccinated: Boolean? = null,
    val sterilized: Boolean? = null,
    val availability: String,
    val photos: List<ImageDTO>,
    val shelter: ShelterDTO,
) : Response()
