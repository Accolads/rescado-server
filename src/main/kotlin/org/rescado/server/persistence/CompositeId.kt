package org.rescado.server.persistence

import java.io.Serializable

open class CompositeId(
    open var identifiable1: Long = -1,
    open var identifiable2: Long = -1,
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CompositeId) return false

        if (identifiable1 != other.identifiable1) return false
        if (identifiable2 != other.identifiable2) return false

        return true
    }

    override fun hashCode(): Int {
        var result = identifiable1.hashCode()
        result = 31 * result + identifiable2.hashCode()
        return result
    }
}
