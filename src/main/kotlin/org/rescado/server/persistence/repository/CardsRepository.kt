package org.rescado.server.persistence.repository

import org.locationtech.jts.geom.Geometry
import org.rescado.server.persistence.entity.Account
import org.rescado.server.persistence.entity.Animal
import org.rescado.server.persistence.entity.Like
import org.rescado.server.persistence.entity.Shelter
import org.rescado.server.persistence.entity.Swipe
import org.rescado.server.util.AreaData
import org.springframework.stereotype.Repository
import java.time.LocalDate
import javax.persistence.EntityManager
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Expression
import javax.persistence.criteria.JoinType
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

@Repository
class CardsRepository(
    val entityManager: EntityManager
) {
    fun findCards(
        account: Account,
        limit: Int,
        location: AreaData,
        kinds: Set<Animal.Kind>,
        sexes: Set<Animal.Sex>,
        minimumAge: Int?,
        maximumAge: Int?,
        minimumWeight: Int?,
        maximumWeight: Int?,
        vaccinated: Boolean?,
        sterilized: Boolean?,
    ): List<Animal> {
        val builder = entityManager.criteriaBuilder
        val query = builder.createQuery(Animal::class.java)

        val animal = query.from(Animal::class.java)

        val shelter = animal.join<Animal, Shelter>("shelter", JoinType.LEFT)

        val likes = animal.join<Animal, Like>("likes", JoinType.LEFT)
        likes.on(builder.equal(likes.get<Account>("account"), account))

        val swipes = animal.join<Animal, Swipe>("swipes", JoinType.LEFT)
        swipes.on(builder.equal(swipes.get<Account>("account"), account))

        val where = mutableListOf<Predicate>().apply {
            add(
                builder.isNull(likes.get<Animal>("animal"))
            )
            add(
                builder.isNull(swipes.get<Animal>("animal"))
            )

            if (location.state == AreaData.State.COMPLETE)
                add(
                    builder.lessThan(
                        distance(builder, shelter.get("geometry"), location.latitude!!, location.longitude!!),
                        builder.literal(location.radius!! * 1000.0)
                    )
                )

            if (kinds.isNotEmpty())
                add(
                    anyFromEnum(builder, kinds, animal, "kind")
                )

            if (sexes.isNotEmpty())
                add(
                    anyFromEnum(builder, sexes, animal, "sex")
                )

            if (minimumAge != null)
                add(
                    builder.lessThanOrEqualTo(
                        animal.get("birthday"),
                        builder.literal(LocalDate.now().minusYears(minimumAge.toLong()))
                    )
                )
            if (maximumAge != null)
                add(
                    builder.greaterThanOrEqualTo(
                        animal.get("birthday"),
                        builder.literal(LocalDate.now().minusYears(maximumAge.toLong()))
                    )
                )

            if (minimumWeight != null)
                add(
                    builder.greaterThanOrEqualTo(
                        animal.get("weight"),
                        builder.literal(minimumWeight)
                    )
                )
            if (maximumWeight != null)
                add(
                    builder.lessThanOrEqualTo(
                        animal.get("weight"),
                        builder.literal(maximumWeight)
                    )
                )

            if (vaccinated != null)
                add(
                    builder.equal(
                        animal.get<Boolean>("vaccinated"),
                        builder.literal(vaccinated)
                    )
                )
            if (sterilized != null)
                add(
                    builder.equal(
                        animal.get<Boolean>("sterilized"),
                        builder.literal(sterilized)
                    )
                )
        }
        query.where(*where.toTypedArray())
        query.orderBy(builder.asc(builder.function<Unit>("RANDOM", null))) // TODO find something more performant than RANDOM()

        val typedQuery = entityManager.createQuery(query).apply {
            maxResults = limit
        }
        return typedQuery.resultList
    }

    private fun anyFromEnum(
        builder: CriteriaBuilder,
        enum: Set<Enum<*>>,
        animal: Root<Animal>,
        column: String,
    ) = builder.or(
        *enum.map {
            builder.equal(
                animal.get<String>(column),
                builder.literal(it.name)
            )
        }.toTypedArray()
    )

    private fun distance(
        builder: CriteriaBuilder,
        point: Expression<Geometry>,
        latitude: Double,
        longitude: Double,
    ) = builder.function(
        "ST_DistanceSphere",
        Double::class.java,
        point,
        builder.function(
            "ST_MakePoint", // TODO Replace with function that takes into account SRID? Or not necessary?
            Geometry::class.java,
            builder.literal(longitude),
            builder.literal(latitude),
        )
    )
}
