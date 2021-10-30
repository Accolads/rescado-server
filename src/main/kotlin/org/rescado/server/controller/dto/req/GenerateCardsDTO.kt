package org.rescado.server.controller.dto.req

import com.fasterxml.jackson.annotation.JsonProperty
import org.rescado.server.controller.dto.validation.AnimalKindSet
import org.rescado.server.controller.dto.validation.AnimalSexSet
import org.rescado.server.controller.dto.validation.InRange
import javax.validation.constraints.Min

data class GenerateCardsDTO(

    @JsonProperty("kinds")
    @get:AnimalKindSet(message = "{AnimalKindSet.GenerateCardsDTO.kinds}")
    val kinds: Set<String>?,

    @JsonProperty("sexes")
    @get:AnimalSexSet(message = "{AnimalSexSet.GenerateCardsDTO.sexes}")
    val sexes: Set<String>?,

    @JsonProperty("minimumAge")
    @get:Min(message = "{Min.GenerateCardsDTO.minimumAge}", value = 1)
    val minimumAge: Int?,

    @JsonProperty("maximumAge")
    @get:Min(message = "{Min.GenerateCardsDTO.maximumAge}", value = 1)
    val maximumAge: Int?,

    @JsonProperty("minimumWeight")
    @get:Min(message = "{Min.GenerateCardsDTO.minimumWeight}", value = 1)
    val minimumWeight: Int?,

    @JsonProperty("maximumWeight")
    @get:Min(message = "{Min.GenerateCardsDTO.maximumWeight}", value = 1)
    val maximumWeight: Int?,

    @JsonProperty("vaccinated")
    val vaccinated: Boolean?,

    @JsonProperty("sterilized")
    val sterilized: Boolean?,

    @JsonProperty("latitude")
    @get:InRange(message = "{InRange.GenerateCardsDTO.latitude}", min = -90.0, max = 90.0)
    val latitude: Double?,

    @JsonProperty("longitude")
    @get:InRange(message = "{InRange.GenerateCardsDTO.longitude}", min = -180.0, max = 180.0)
    val longitude: Double?,

    @JsonProperty("radius")
    @get:Min(message = "{Min.GenerateCardsDTO.radius}", value = 10)
    val radius: Int?,
)
