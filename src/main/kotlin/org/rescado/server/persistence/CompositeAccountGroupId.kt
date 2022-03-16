package org.rescado.server.persistence

import java.io.Serializable

open class CompositeAccountGroupId(
    open var account: Long = -1,
    open var group: Long = -1,
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CompositeAccountGroupId) return false

        if (account != other.account) return false
        if (group != other.group) return false

        return true
    }

    override fun hashCode(): Int {
        var result = account.hashCode()
        result = 31 * result + group.hashCode()
        return result
    }
}
