package org.rescado.server.persistence.entity

import org.locationtech.jts.geom.Point
import org.rescado.server.persistence.Identifiable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "shelter")
open class Shelter(

    @Column(name = "name")
    open var name: String,

    @Column(name = "email")
    open var email: String,

    @Column(name = "website")
    open var website: String,

    @Column(name = "newsfeed")
    open var newsfeed: String?,

    @Column(name = "address")
    open var address: String,

    @Column(name = "postal_code")
    open var postalCode: String,

    @Column(name = "city")
    open var city: String,

    @Column(name = "country")
    open var country: String,

    // TODO add @Type so IDE doesn't complain tho without seems to be working just fine
    @Column(name = "geometry")
    open var geometry: Point,

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "logo_image_id", referencedColumnName = "id")
    open var logo: Image,

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "banner_image_id", referencedColumnName = "id")
    open var banner: Image?,

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "shelter")
    open var animals: MutableSet<Animal>,

) : Identifiable()
