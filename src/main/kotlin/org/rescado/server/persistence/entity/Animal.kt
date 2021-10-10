package org.rescado.server.persistence.entity

import org.rescado.server.persistence.Identifiable
import java.time.ZonedDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "animal")
open class Animal(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelter_id")
    open var shelter: Shelter,

    @Column(name = "kind")
    @Enumerated(EnumType.STRING)
    open var kind: AnimalKind,

    @Column(name = "breed")
    open var breed: String,

    @Column(name = "name")
    open var name: String,

    @Column(name = "description")
    open var description: String,

    @Column(name = "sex")
    @Enumerated(EnumType.STRING)
    open var sex: AnimalSex,

    @Column(name = "birthday")
    open var birthday: ZonedDateTime,

    @Column(name = "weight")
    open var weight: Int,

    @Column(name = "vaccinated")
    open var vaccinated: Boolean,

    @Column(name = "sterilized")
    open var sterilized: Boolean,

    @OneToMany
    @JoinTable(
        name = "photos",
        joinColumns = [JoinColumn(name = "animal_id", referencedColumnName = "id") ],
        inverseJoinColumns = [JoinColumn(name = "image_id", referencedColumnName = "id") ]
    )
    open var photos: MutableSet<Image>,

    @OneToMany(mappedBy = "animal")
    open var likes: MutableSet<Like>,

) : Identifiable()

enum class AnimalKind {
    CAT,
    DOG,
}

enum class AnimalSex {
    FEMALE,
    MALE,
}
