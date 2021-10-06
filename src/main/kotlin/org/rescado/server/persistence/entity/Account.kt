package org.rescado.server.persistence.entity

import org.rescado.server.persistence.Identifiable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Table

@Entity
@Table(name = "account")
open class Account(

    @Column(name = "uuid")
    open var uuid: String,

    @Column(name = "email")
    open var email: String?,

    @Column(name = "password")
    open var password: String?,

    @Column(name = "name")
    open var name: String?,

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    open var status: AccountStatus,

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(
//        name = "likes",
//        joinColumns = [JoinColumn(name = "account_id", referencedColumnName = "id")],
//        inverseJoinColumns = [JoinColumn(name = "animal_id", referencedColumnName = "id")]
//    )
//    open var subscriptions: MutableSet<Animal>,

) : Identifiable()

enum class AccountStatus {
    ENABLED,
    DISABLED,
}
