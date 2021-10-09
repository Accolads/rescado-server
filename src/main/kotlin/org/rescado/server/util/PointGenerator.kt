package org.rescado.server.util

import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Point
import org.locationtech.jts.geom.PrecisionModel
import org.springframework.stereotype.Component

@Component
class PointGenerator {

    private val geometryFactory: GeometryFactory = GeometryFactory(PrecisionModel(PrecisionModel.FLOATING), 4326)

    fun make(latitude: Double?, longitude: Double?): Point? = if (latitude == null || longitude == null) null else geometryFactory.createPoint(Coordinate(longitude, latitude))
}
