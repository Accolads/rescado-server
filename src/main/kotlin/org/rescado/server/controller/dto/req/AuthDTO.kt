package org.rescado.server.controller.dto.req

abstract class AuthDTO(
    open val latitude: Float?,
    open val longitude: Float?,
) {
    fun hasCoordinates(): Boolean {
        return this.latitude != null && this.longitude != null
    }

    fun hasPartialCoordinates(): Boolean {
        return (this.latitude != null && this.longitude == null) || (this.latitude == null && this.longitude != null)
    }
}
