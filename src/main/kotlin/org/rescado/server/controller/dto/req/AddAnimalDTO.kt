package org.rescado.server.controller.dto.req

import com.fasterxml.jackson.annotation.JsonProperty
import org.rescado.server.constant.SecurityConstants
import org.rescado.server.controller.dto.validation.AnimalAvailability
import org.rescado.server.controller.dto.validation.AnimalKind
import org.rescado.server.controller.dto.validation.AnimalSex
import java.time.LocalDate
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Past
import javax.validation.constraints.Size

data class AddAnimalDTO(

    @JsonProperty("kind")
    @get:NotBlank(message = "{NotBlank.AddAnimalDTO.kind}")
    @get:AnimalKind(message = "{AnimalKind.AddAnimalDTO.kind}")
    val kind: String?,

    @JsonProperty("breed")
    @get:NotBlank(message = "{NotBlank.AddAnimalDTO.breed}")
    val breed: String?,

    @JsonProperty("name")
    @get:NotBlank(message = "{NotBlank.AddAnimalDTO.name}")
    val name: String?,

    @JsonProperty("description")
    @get:NotBlank(message = "{NotBlank.AddAnimalDTO.description}")
    val description: String?,

    @JsonProperty("sex")
    @get:NotBlank(message = "{NotBlank.AddAnimalDTO.sex}")
    @get:AnimalSex(message = "{AnimalSex.AddAnimalDTO.sex}")
    val sex: String?,

    @JsonProperty("birthday")
    @get:NotNull(message = "{NotNull.AddAnimalDTO.birthday}")
    @get:Past(message = "{Past.AddAnimalDTO.birthday}")
    val birthday: LocalDate?,

    @JsonProperty("weight")
    @get:NotNull(message = "{NotNull.AddAnimalDTO.weight}")
    @get:Min(message = "{Min.AddAnimalDTO.weight}", value = 1)
    val weight: Int?,

    @JsonProperty("vaccinated")
    @get:NotNull(message = "{NotNull.AddAnimalDTO.vaccinated}")
    val vaccinated: Boolean?,

    @JsonProperty("sterilized")
    @get:NotNull(message = "{NotNull.AddAnimalDTO.sterilized}")
    val sterilized: Boolean?,

    @JsonProperty("availability")
    @get:NotBlank(message = "{NotBlank.AddAnimalDTO.availability}")
    @get:AnimalAvailability(message = "{AnimalAvailability.PatchAnimalDTO.availability}")
    val availability: String?,

    @JsonProperty("photos")
    @get:NotEmpty(message = "{NotEmpty.AddAnimalDTO.photos}")
    @get:Size(message = "{Size.AddAnimalDTO.photos}", max = SecurityConstants.IMAGE_MAX_REFERENCES)
    val photos: List<String>?
)
