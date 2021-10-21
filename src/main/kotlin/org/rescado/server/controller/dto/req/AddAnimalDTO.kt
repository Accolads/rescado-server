package org.rescado.server.controller.dto.req

import com.fasterxml.jackson.annotation.JsonProperty
import org.rescado.server.controller.dto.validation.AnimalKind
import org.rescado.server.controller.dto.validation.AnimalSex
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
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
    @get:NotBlank(message = "{NotBlank.AddAnimalDTO.birthday}")
    @get:Pattern(message = "{Pattern.AddAnimalDTO.birthday}", regexp = "^20\\d{2}-[0-1][1-9]-[0-3]\\d\$")
    val birthday: String?,

    @JsonProperty("weight")
    @get:NotNull(message = "{NotNull.AddAnimalDTO.weight}")
    @get:Min(value = 1, message = "{Min.AddAnimalDTO.weight}")
    val weight: Int?,

    @JsonProperty("vaccinated")
    @get:NotNull(message = "{NotNull.AddAnimalDTO.vaccinated}")
    val vaccinated: Boolean?,

    @JsonProperty("sterilized")
    @get:NotNull(message = "{NotNull.AddAnimalDTO.sterilized}")
    val sterilized: Boolean?,

    @JsonProperty("photos")
    @get:NotEmpty(message = "{NotEmpty.AddAnimalDTO.photos}")
    @get:Size(message = "{Size.AddAnimalDTO.photos}", max = 10)
    val photos: List<String>?
)
