package org.rescado.server.persistence.entity

import org.rescado.server.persistence.CompositeId
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "membership")
@IdClass(CompositeId::class)
open class Membership(

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    open var account: Account,

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    open var group: Group,

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    open var status: Status,

) {

    enum class Status {
        INVITED,
        CONFIRMED,
    }
}
