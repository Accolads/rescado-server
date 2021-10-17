package org.rescado.server.persistence.entity

import org.rescado.server.persistence.Identifiable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "account")
open class Account(

    @Column(name = "uuid")
    open var uuid: String,

    @Column(name = "name")
    open var name: String?,

    @Column(name = "email")
    open var email: String?,

    @Column(name = "password")
    open var password: String?,

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    open var status: Status,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelter_id", referencedColumnName = "id")
    open var shelter: Shelter?, // if not null, this is a volunteer

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avatar_image_id", referencedColumnName = "id")
    open var avatar: Image?,

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "favorites",
        joinColumns = [JoinColumn(name = "account_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "shelter_id", referencedColumnName = "id")]
    )
    open var favorites: MutableSet<Shelter>,

    @OneToMany(mappedBy = "account")
    open var likes: MutableSet<Like>,

    @OneToMany(mappedBy = "account")
    open var swipes: MutableSet<Swipe>,

) : Identifiable() {

    enum class Status {
        ANONYMOUS,
        ENROLLED,
        BLOCKED,
    }
}
