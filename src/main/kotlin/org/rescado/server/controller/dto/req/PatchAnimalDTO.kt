package org.rescado.server.controller.dto.req

import com.fasterxml.jackson.annotation.JsonProperty
import org.rescado.server.controller.dto.validation.AnimalAvailability
import org.rescado.server.controller.dto.validation.AnimalKind
import org.rescado.server.controller.dto.validation.AnimalSex
import java.time.LocalDate
import javax.validation.constraints.Min
import javax.validation.constraints.Past

data class PatchAnimalDTO(

    @JsonProperty("kind")
    @get:AnimalKind(message = "{AnimalKind.PatchAnimalDTO.kind}")
    val kind: String?,

    @JsonProperty("breed")
    val breed: String?,

    @JsonProperty("name")
    val name: String?,

    @JsonProperty("description")
    val description: String?,

    @JsonProperty("sex")
    @get:AnimalSex(message = "{AnimalSex.PatchAnimalDTO.sex}")
    val sex: String?,

    @JsonProperty("birthday")
    @get:Past(message = "{Past.PatchAnimalDTO.birthday}")
    val birthday: LocalDate?,

    @JsonProperty("weight")
    @get:Min(value = 1, message = "{Min.PatchAnimalDTO.weight")
    val weight: Int?,

    @JsonProperty("vaccinated")
    val vaccinated: Boolean?,

    @JsonProperty("sterilized")
    val sterilized: Boolean?,

    @JsonProperty("availability")
    @get:AnimalAvailability(message = "{AnimalAvailability.PatchAnimalDTO.availability}")
    val availability: String?,
)
