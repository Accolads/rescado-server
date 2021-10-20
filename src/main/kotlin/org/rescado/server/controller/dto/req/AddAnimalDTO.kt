package org.rescado.server.controller.dto.req

import org.rescado.server.controller.dto.validation.AnimalKind
import org.rescado.server.controller.dto.validation.AnimalSex
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

data class AddAnimalDTO(

    @get:NotBlank(message = "{NotBlank.AddAnimalDTO.kind}")
    @get:AnimalKind(message = "{AnimalKind.AddAnimalDTO.kind")
    val kind: String?,

    @get:NotBlank(message = "{NotBlank.AddAnimalDTO.breed}")
    val breed: String?,

    @get:NotBlank(message = "{NotBlank.AddAnimalDTO.name}")
    val name: String?,

    @get:NotBlank(message = "{NotBlank.AddAnimalDTO.description}")
    val description: String?,

    @get:NotBlank(message = "{NotBlank.AddAnimalDTO.sex}")
    @get:AnimalSex(message = "{AnimalSex.AddAnimalDTO.sex")
    val sex: String?,

    @get:NotBlank(message = "{NotBlank.AddAnimalDTO.birthday}")
    @get:Pattern(message = "{Pattern.AddAnimalDTO.birthday}", regexp = "^20\\d{2}-[0-1][1-9]-[0-3]\\d\$")
    val birthday: String?,

    @get:NotNull(message = "{NotNull.AddAnimalDTO.weight}")
    @get:Min(value = 1, message = "{Min.AddAnimalDTO.weight")
    val weight: Int?,

    @get:NotNull(message = "{NotNull.AddAnimalDTO.vaccinated}")
    val vaccinated: Boolean?,

    @get:NotNull(message = "{NotNull.AddAnimalDTO.sterilized}")
    val sterilized: Boolean?,

    @get:NotEmpty(message = "{NotEmpty.AddAnimalDTO.photos}")
    val photos: List<String>?
)
