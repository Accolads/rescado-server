package org.rescado.server.persistence.entity

import org.rescado.server.persistence.Identifiable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "likes")
open class Like(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    open var account: Account,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id")
    open var animal: Animal,

    @Column(name = "chat_ref")
    open var uuid: String?,

    @Column(name = "unread_count")
    open var unreadCount: Int,

) : Identifiable()
