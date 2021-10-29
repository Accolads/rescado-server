package org.rescado.server.util

data class AreaData(
    val latitude: Double?,
    val longitude: Double?,
    val radius: Int?,
) {

    val state: State
        get() {
            if (latitude != null && longitude != null && radius != null)
                return State.COMPLETE
            if (latitude == null && longitude == null && radius == null)
                return State.EMPTY
            return State.INCOMPLETE
        }

    enum class State {
        INCOMPLETE,
        COMPLETE,
        EMPTY,
    }
}
