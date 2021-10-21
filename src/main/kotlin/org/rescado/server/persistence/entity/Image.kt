package org.rescado.server.persistence.entity

import org.rescado.server.persistence.Identifiable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Table

@Entity
@Table(name = "image")
open class Image(

    @Column(name = "reference")
    open var reference: String,

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    open var type: Type,

    @Column(name = "source")
    @Enumerated(EnumType.STRING)
    open var source: Source,

) : Identifiable() {

    enum class Type {
        AVATAR, // Account profile picture
        LOGO, // Shelter logo
        BANNER, // Shelter page banner
        PHOTO, // Animal photo
    }

    enum class Source {
        EXTERNAL, // hosted on external site
        INTERNAL, // in our Firebase Cloud Storage
    }
}
