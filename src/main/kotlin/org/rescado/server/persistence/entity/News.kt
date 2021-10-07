package org.rescado.server.persistence.entity

import org.rescado.server.persistence.Identifiable
import java.time.ZonedDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "news")
open class News(

    @Column(name = "title")
    open var title: String,

    @Column(name = "body")
    open var body: String,

    @Column(name = "timestamp")
    open var timestamp: ZonedDateTime,

) : Identifiable()
