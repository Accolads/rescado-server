package org.rescado.server.persistence

import java.io.Serializable

open class CompositeAccountAnimalId(
    open var account: Long = -1,
    open var animal: Long = -1,
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CompositeAccountAnimalId) return false

        if (account != other.account) return false
        if (animal != other.animal) return false

        return true
    }

    override fun hashCode(): Int {
        var result = account.hashCode()
        result = 31 * result + animal.hashCode()
        return result
    }
}
