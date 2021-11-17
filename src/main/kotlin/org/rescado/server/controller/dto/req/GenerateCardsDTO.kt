package org.rescado.server.controller.dto.req

import com.fasterxml.jackson.annotation.JsonProperty
import org.rescado.server.constant.SecurityConstants
import org.rescado.server.controller.dto.validation.AnimalKind
import org.rescado.server.controller.dto.validation.AnimalSex
import org.rescado.server.controller.dto.validation.InRange
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

data class GenerateCardsDTO(

    @JsonProperty("number")
    @get:NotNull(message = "{NotNull.GenerateCardsDTO.number}")
    @get:Max(message = "{Max.GenerateCardsDTO.number}", value = SecurityConstants.CARDS_LIMIT.toLong())
    val number: Int?,

    @JsonProperty("kinds")
    @get:AnimalKind(message = "{AnimalKind.GenerateCardsDTO.kinds}")
    val kinds: Set<String>?,

    @JsonProperty("sexes")
    @get:AnimalSex(message = "{AnimalSex.GenerateCardsDTO.sexes}")
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
