package org.rescado.server.persistence.entity

import org.rescado.server.persistence.Identifiable
import java.time.ZonedDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Table

@Entity
@Table(name = "news")
open class News(

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    open var type: Type,

    @Column(name = "timestamp")
    open var timestamp: ZonedDateTime,

    @Column(name = "reference")
    open var reference: Long,

) : Identifiable() {

    enum class Type {
        SHELTER_NEW_ANIMAL, // The shelter added a new animal
        SHELTER_AVAILABLE_ANIMAL, // The shelter marked an animal as available
        ANIMAL_NEW_PHOTO, // The animal had a new photo uploaded for it
        ANIMAL_ADOPTED, // The animal was adopted
    }
}
