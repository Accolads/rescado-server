package org.rescado.server.controller.dto.req

abstract class AuthDTO(
    open val latitude: Double?,
    open val longitude: Double?,
) {
    fun hasCoordinates() = this.latitude != null && this.longitude != null

    fun hasPartialCoordinates() = (this.latitude != null && this.longitude == null) || (this.latitude == null && this.longitude != null)
}
