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

    @Column(name = "url")
    open var url: String,

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    open var type: ImageType,

    @Column(name = "source")
    @Enumerated(EnumType.STRING)
    open var source: ImageSource,

) : Identifiable()

enum class ImageType {
    AVATAR, // Account profile picture
    LOGO, // Shelter logo
    BANNER, // Shelter page banner
    PHOTO, // Animal photo
}

enum class ImageSource {
    EXTERNAL, // hosted on external site
    FIREBASE, // in our Firebase Cloud Storage
}
