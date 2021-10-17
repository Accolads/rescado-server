package org.rescado.server.service

import org.locationtech.jts.geom.Point
import org.rescado.server.persistence.entity.Image
import org.rescado.server.persistence.entity.Shelter
import org.rescado.server.persistence.repository.ShelterRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class ShelterService(
    private val shelterRepository: ShelterRepository,
    private val imageService: ImageService,
) {

    fun getById(id: Long): Shelter? = shelterRepository.findById(id).orElse(null)

    fun getAll(): MutableList<Shelter> = shelterRepository.findAll()

    fun create(
        name: String,
        email: String,
        website: String,
        newsfeed: String?,
        address: String,
        postalCode: String,
        city: String,
        country: String,
        geometry: Point,
        logo: Image,
        banner: Image?,
    ): Shelter {
        val shelter = Shelter(
            name = name,
            email = email,
            website = website,
            newsfeed = newsfeed,
            address = address,
            postalCode = postalCode,
            city = city,
            country = country,
            geometry = geometry,
            logo = logo,
            banner = banner,
            animals = mutableSetOf(),
        )
        return shelterRepository.save(shelter)
    }

    fun update(
        shelter: Shelter,
        name: String?,
        email: String?,
        website: String?,
        newsfeed: String?,
        address: String?,
        postalCode: String?,
        city: String?,
        country: String?,
        geometry: Point?,
        logo: Image?,
        banner: Image?,
    ): Shelter {
        name?.let { shelter.name = it }
        email?.let { shelter.email = it }
        website?.let { shelter.website = it }
        newsfeed?.let { shelter.newsfeed = it }
        address?.let { shelter.address = it }
        postalCode?.let { shelter.postalCode = it }
        city?.let { shelter.city = it }
        country?.let { shelter.country = it }
        geometry?.let { shelter.geometry = it }
        logo?.let {
            imageService.delete(shelter.logo)
            shelter.logo = it
        }
        banner?.let {
            shelter.banner?.let { oldBanner -> imageService.delete(oldBanner) }
            shelter.banner = it
        }

        return shelterRepository.save(shelter)
    }

    fun delete(shelter: Shelter) = shelterRepository.delete(shelter)
}
