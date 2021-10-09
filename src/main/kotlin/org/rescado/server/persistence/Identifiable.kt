package org.rescado.server.persistence

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class Identifiable {

    @GenericGenerator(
        name = "id_generator",
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = [
            Parameter(name = "sequence_name", value = "id_sequence"),
            Parameter(name = "initial_value", value = "1000"),
            Parameter(name = "increment_size", value = "1")
        ]
    )
    @GeneratedValue(generator = "id_generator")
    @Id
    @Column(name = "id")
    open var id: Long = 0

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Identifiable
        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

open class CompositeAccountAnimalId(
    open var account: Long,
    open var animal: Long,
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
