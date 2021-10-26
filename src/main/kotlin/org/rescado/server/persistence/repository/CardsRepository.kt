package org.rescado.server.persistence.repository

import org.locationtech.jts.geom.Geometry
import org.rescado.server.persistence.entity.Account
import org.rescado.server.persistence.entity.Animal
import org.rescado.server.persistence.entity.Like
import org.rescado.server.persistence.entity.Shelter
import org.rescado.server.persistence.entity.Swipe
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
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
        latitude: Double? = null,
        longitude: Double? = null,
        radius: Int? = null,
    ): List<Animal> {
        val builder = entityManager.criteriaBuilder
        val query: CriteriaQuery<Animal> = builder.createQuery(Animal::class.java)

        val animal: Root<Animal> = query.from(Animal::class.java)

        val shelter = animal.join<Animal, Shelter>("shelter", JoinType.LEFT)

        val likes = animal.join<Animal, Like>("likes", JoinType.LEFT)
        likes.on(builder.equal(likes.get<Account>("account"), account))

        val swipes = animal.join<Animal, Swipe>("swipes", JoinType.LEFT)
        swipes.on(builder.equal(swipes.get<Account>("account"), account))

        val where = mutableListOf<Predicate>().apply {
            add(builder.isNull(likes.get<Animal>("animal")))
            add(builder.isNull(swipes.get<Animal>("animal")))

            if (latitude != null && longitude != null && radius != null)
                add(builder.lessThan(distance(builder, shelter.get("geometry"), latitude, longitude), builder.literal(radius * 1000.0)))
        }
        query.where(*where.toTypedArray())
        query.orderBy(builder.asc(builder.function<Unit>("RANDOM", null))) // TODO find something more performant

        val typedQuery = entityManager.createQuery(query).apply {
            maxResults = limit
        }
        return typedQuery.resultList
    }

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
            "ST_MakePoint",
            Geometry::class.java,
            builder.literal(longitude),
            builder.literal(latitude),
        )
    )
}
