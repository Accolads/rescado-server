package org.rescado.server.controller.dto.res

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDate

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AnimalDTO(
    val id: Long,
    val name: String,
    val description: String,
    val kind: String,
    val breed: String,
    val sex: String,
    val birthday: LocalDate,
    val weight: Int,
    val vaccinated: Boolean,
    val sterilized: Boolean,
    val availability: String,
    val photos: List<ImageDTO>,
    val shelter: ShelterDTO,
) : Response()
