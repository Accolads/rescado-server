package org.rescado.server.controller.dto.res

data class AnimalDTO(
    val id: Long,
    val name: String,
    val kind: String,
    val breed: String,
    val sex: String,
    val age: Int,
    val weight: Int,
    val vaccinated: Boolean,
    val sterilized: Boolean,
    val photos: List<String>,
    val shelter: ShelterDTO,
) : Response()
