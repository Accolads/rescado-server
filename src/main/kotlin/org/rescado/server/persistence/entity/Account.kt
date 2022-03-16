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

    @Column(name = "apple_reference")
    open var appleReference: String?,

    @Column(name = "google_reference")
    open var googleReference: String?,

    @Column(name = "facebook_reference")
    open var facebookReference: String?,

    @Column(name = "twitter_reference")
    open var twitterReference: String?,

    @Column(name = "email")
    open var email: String?,

    @Column(name = "password")
    open var password: String?,

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    open var status: Status,

    // TODO This class should not have any FetchType set to EAGER because memberships and shelter and avatar EACH trigger a join every time JWT is checked aka basically every request.
    // Just changing it to LAZY here will cause exceptions in classes that just assume every field is set when Account is fetched from the db, so some refactoring in the controllers/services is required.

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "account")
    open var memberships: MutableSet<Membership>,

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shelter_id", referencedColumnName = "id")
    open var shelter: Shelter?, // if not null, this is a volunteer

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "avatar_image_id", referencedColumnName = "id")
    open var avatar: Image?,

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "following",
        joinColumns = [JoinColumn(name = "account_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "shelter_id", referencedColumnName = "id")]
    )
    open var following: MutableSet<Shelter>,

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
    open var likes: MutableSet<Like>,

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
    open var swipes: MutableSet<Swipe>,

) : Identifiable() {

    fun hasAtLeastOneReference() = appleReference != null || googleReference != null || facebookReference != null || twitterReference != null || email != null

    enum class Status {
        ANONYMOUS,
        ENROLLED,
        VOLUNTEER,
        BLOCKED,
    }
}
