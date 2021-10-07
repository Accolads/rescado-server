package org.rescado.server.persistence.entity

import org.rescado.server.persistence.Identifiable
import java.time.ZonedDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "swipes")
open class Swipe(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    open var account: Account,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id")
    open var animal: Animal,

    @Column(name = "last_login")
    open var timestamp: ZonedDateTime,

) : Identifiable()
