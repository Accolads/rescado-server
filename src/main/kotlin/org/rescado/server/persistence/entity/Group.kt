package org.rescado.server.persistence.entity

import org.rescado.server.persistence.Identifiable
import java.time.ZonedDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "group")
open class Group(

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "group")
    open var memberships: MutableSet<Membership>,

    @Column(name = "created")
    open var created: ZonedDateTime,

) : Identifiable()
