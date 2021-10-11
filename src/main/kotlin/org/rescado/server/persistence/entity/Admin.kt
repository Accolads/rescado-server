package org.rescado.server.persistence.entity

import org.rescado.server.persistence.Identifiable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "admin")
open class Admin(

    @Column(name = "username")
    open var username: String,

    @Column(name = "password")
    open var password: String,

) : Identifiable()
