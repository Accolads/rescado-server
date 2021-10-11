package org.rescado.server.controller

import org.rescado.server.constant.SecurityConstants
import org.rescado.server.controller.dto.res.InfoDTO
import org.rescado.server.controller.dto.res.Response
import org.rescado.server.persistence.entity.Animal
import org.rescado.server.persistence.entity.Image
import org.rescado.server.persistence.entity.Shelter
import org.rescado.server.persistence.repository.AnimalRepository
import org.rescado.server.persistence.repository.ImageRepository
import org.rescado.server.persistence.repository.ShelterRepository
import org.rescado.server.service.SessionService
import org.rescado.server.util.PointGenerator
import org.rescado.server.util.generateResponse
import org.springframework.boot.info.BuildProperties
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.ZoneOffset
import java.time.ZonedDateTime

@RestController
@RequestMapping(SecurityConstants.INFO_ROUTE)
class InfoController(
    private val sessionService: SessionService,
    private val buildProperties: BuildProperties,

    private val imageRepository: ImageRepository,
    private val shelterRepository: ShelterRepository,
    private val animalRepository: AnimalRepository,
    private val pointGenerator: PointGenerator,
) {

    @GetMapping
    fun getInfo(): ResponseEntity<Response> {
        return generateResponse(
            InfoDTO(
                name = "rescado-${buildProperties.name}",
                version = buildProperties.version,
                build = buildProperties.time,
                clock = ZonedDateTime.now(),
                sessions = sessionService.getNumberOfActiveSessions(),
            )
        )
    }

    @GetMapping("/setup")
    fun setup(): String {

        val logo = Image(
            url = "https://dierenasielgenk.be/wp-content/uploads/2019/02/DierenasielGenkLogoSquare150.jpg",
            type = Image.Type.LOGO,
            source = Image.Source.EXTERNAL,
        )
        imageRepository.save(logo)

        val genk = Shelter(
            name = "Dierenasiel Genk vzw",
            email = "info@dierenasielgenk.be.fake",
            website = "https://dierenasielgenk.be",
            newsfeed = "https://dierenasielgenk.be/feed/",
            address = "Europalaan 13",
            postalCode = "3600",
            city = "Genk",
            country = "Belgium",
            geometry = pointGenerator.make(50.966878038847376, 5.4864454777316585)!!,
            logo = logo,
            banner = null,
            animals = mutableSetOf(),
        )
        shelterRepository.save(genk)

        val milo1 = Image(
            url = "https://dierenasielgenk.be/wp-content/uploads/2021/10/IMG_7404-scaled.jpg",
            type = Image.Type.PHOTO,
            source = Image.Source.EXTERNAL,
        )
        imageRepository.save(milo1)

        val milo2 = Image(
            url = "https://dierenasielgenk.be/wp-content/uploads/2021/10/IMG_7385-1200x800.jpg",
            type = Image.Type.PHOTO,
            source = Image.Source.EXTERNAL,
        )
        imageRepository.save(milo2)

        val milo = Animal(
            shelter = genk,
            kind = Animal.Kind.DOG,
            breed = "X Border Collie",
            name = "Milo",
            description = "Deze grappige Border Collie kruising heet Milo en samen met Mirko is hij bij ons afgestaan.\n" +
                "Het klopt dat Milo en Mirko apart op de website staan doordat we hen niet samen herplaatsen.\n" +
                "Milo heeft namelijk een grote liefde, lees: verslaving, voor tennisballen en speeltjes. Wanneer hij in aanraking komt met speeltjes wordt hij daar hypernerveus van en dringt hij het ‘spelen’ bij de mensen op. Dit ongewenste gedrag dat ook gepaard gaat met veel vocaliteit nam Mirko wat over en hij werd dan vervolgens nerveus van Milo.\n" +
                "Zonder speeltjes in zijn buurt wil Milo graag bij de mensen komen en gaat zijn neus aan het werk. Bij rust is Milo ook voergevoelig en hij kent de commando’s ZIT en AF al. Lekker kauwen op een knabbeloor wil hij dan ook graag en daardoor kan hij vervolgens meer ontspannen.\n" +
                "Het vinden van rust gaat een werkpunt zijn voor Milo. Met rust, structuur en de juiste sturing hopen we dat Milo dit kan gaan vinden.\n" +
                "Milo is wel een actieve hond en lekker met hem gaan speuren of wandelingen maken in een rustige omgeving zullen leuke, gezamenlijke bezigheden zijn.\n" +
                "We zoeken voor Milo een huis met een tuin en een rustig gezin. Milo heeft in zijn vorige thuis met een kat geleefd en dit is dan ook een optie.",
            sex = Animal.Sex.MALE,
            birthday = ZonedDateTime.of(2018, 1, 3, 0, 0, 0, 0, ZoneOffset.UTC),
            weight = 27,
            vaccinated = true,
            sterilized = true,
            likes = mutableSetOf(),
            photos = mutableSetOf(milo1, milo2)
        )
        animalRepository.save(milo)

        return "Ok"
    }
}
