package org.rescado.server.controller.dto.req

import org.rescado.server.controller.dto.validation.AnimalKind
import org.rescado.server.controller.dto.validation.AnimalSex
import javax.validation.constraints.Min
import javax.validation.constraints.Pattern

data class PatchAnimalDTO(

    @get:AnimalKind(message = "{AnimalKind.PatchAnimalDTO.kind")
    val kind: String?,

    val breed: String?,

    val name: String?,

    val description: String?,

    @get:AnimalSex(message = "{AnimalSex.PatchAnimalDTO.sex")
    val sex: String?,

    @get:Pattern(message = "{Pattern.PatchAnimalDTO.birthday}", regexp = "^20\\d{2}-[0-1][1-9]-[0-3]\\d\$")
    val birthday: String?,

    @get:Min(value = 1, message = "{Min.PatchAnimalDTO.weight")
    val weight: Int,

    val vaccinated: Boolean?,

    val sterilized: Boolean?,

)
