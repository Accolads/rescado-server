package org.rescado.server.persistence.entity

import org.rescado.server.persistence.CompositeId
import java.time.ZonedDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "swipes")
@IdClass(CompositeId::class)
open class Swipe(

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    open var account: Account,

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id")
    open var animal: Animal,

    @Column(name = "timestamp")
    open var timestamp: ZonedDateTime,

)
