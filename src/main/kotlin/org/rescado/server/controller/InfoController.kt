package org.rescado.server.controller

import org.rescado.server.controller.dto.res.InfoDTO
import org.rescado.server.controller.dto.res.Response
import org.rescado.server.persistence.entity.Animal
import org.rescado.server.persistence.entity.AnimalKind
import org.rescado.server.persistence.entity.AnimalSex
import org.rescado.server.persistence.entity.Image
import org.rescado.server.persistence.entity.ImageSource
import org.rescado.server.persistence.entity.ImageType
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
@RequestMapping("/info")
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
            type = ImageType.LOGO,
            source = ImageSource.EXTERNAL,
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
            type = ImageType.PHOTO,
            source = ImageSource.EXTERNAL,
        )
        imageRepository.save(milo1)

        val milo2 = Image(
            url = "https://dierenasielgenk.be/wp-content/uploads/2021/10/IMG_7385-1200x800.jpg",
            type = ImageType.PHOTO,
            source = ImageSource.EXTERNAL,
        )
        imageRepository.save(milo2)

        val milo = Animal(
            shelter = genk,
            kind = AnimalKind.DOG,
            breed = "X Border Collie",
            name = "Milo",
            description = "Deze grappige Border Collie kruising heet Milo en samen met Mirko is hij bij ons afgestaan.\n" +
                "Het klopt dat Milo en Mirko apart op de website staan doordat we hen niet samen herplaatsen.\n" +
                "Milo heeft namelijk een grote liefde, lees: verslaving, voor tennisballen en speeltjes. Wanneer hij in aanraking komt met speeltjes wordt hij daar hypernerveus van en dringt hij het ‘spelen’ bij de mensen op. Dit ongewenste gedrag dat ook gepaard gaat met veel vocaliteit nam Mirko wat over en hij werd dan vervolgens nerveus van Milo.\n" +
                "Zonder speeltjes in zijn buurt wil Milo graag bij de mensen komen en gaat zijn neus aan het werk. Bij rust is Milo ook voergevoelig en hij kent de commando’s ZIT en AF al. Lekker kauwen op een knabbeloor wil hij dan ook graag en daardoor kan hij vervolgens meer ontspannen.\n" +
                "Het vinden van rust gaat een werkpunt zijn voor Milo. Met rust, structuur en de juiste sturing hopen we dat Milo dit kan gaan vinden.\n" +
                "Milo is wel een actieve hond en lekker met hem gaan speuren of wandelingen maken in een rustige omgeving zullen leuke, gezamenlijke bezigheden zijn.\n" +
                "We zoeken voor Milo een huis met een tuin en een rustig gezin. Milo heeft in zijn vorige thuis met een kat geleefd en dit is dan ook een optie.",
            sex = AnimalSex.MALE,
            birthday = ZonedDateTime.of(2018, 1, 3, 0, 0, 0, 0, ZoneOffset.UTC),
            weight = 27,
            vaccinated = true,
            sterilized = true,
            likes = mutableSetOf(),
            photos = mutableSetOf(milo1, milo2)
        )
        animalRepository.save(milo)

        val yako1 = Image(
            url = "http://static.ace-charity.org/onderhoud/pics/dogs/medium/Yakovlev_1_ref_nr_15262_2020-01-11-15-50-54.jpg",
            type = ImageType.PHOTO,
            source = ImageSource.EXTERNAL,
        )
        imageRepository.save(yako1)

        val yako2 = Image(
            url = "http://static.ace-charity.org/onderhoud/pics/dogs/medium/Yakovlev_2_ref_nr_15262_2018-09-28-16-34-45.jpg",
            type = ImageType.PHOTO,
            source = ImageSource.EXTERNAL,
        )
        imageRepository.save(yako2)

        val yako = Animal(
            shelter = genk,
            kind = AnimalKind.DOG,
            breed = "X German Shepherd",
            name = "Yako",
            description = "Deze knappe bink werd gered uit een dodingstation. Yakovlev heeft veel energie, staat graag vooraan in de rij en laat merken dat hij aanwezig is.\n" +
                "Hij vertroeft heel graag bij mensen en tracht naar een actief baasje die met hem wil bezig zijn. \n " + "" +
                "Yakovlev zal snel nieuwe zaken aanleren en voor zijn baasje willen werken want hij is intelligent en bereidwillig (Herder-eigenschappen). \n" +
                "Hij is goed met andere honden, maar soms een beetje ontstuimig in het spel en dat wordt niet altijd door andere honden gewaardeerd. \n" +
                "Voor kleine kindjes is hij misschien wat te bruusk. Yakovlev is een imposante verschijning, maar heeft een tof en braaf karakter. \n" +
                "Voor wie van een maatje groter houdt en een trouwe kameraad wilt, Yakovlev staat alvast te popelen om aan een nieuw leven te mogen beginnen...",
            sex = AnimalSex.MALE,
            birthday = ZonedDateTime.of(2018, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC),
            weight = 48,
            vaccinated = true,
            sterilized = true,
            likes = mutableSetOf(),
            photos = mutableSetOf(yako1, yako2)
        )
        animalRepository.save(yako)

        val boelie1 = Image(
            url = "https://dierenasielgenk.be/wp-content/uploads/2020/11/boelie-1.jpg",
            type = ImageType.PHOTO,
            source = ImageSource.EXTERNAL,
        )
        imageRepository.save(boelie1)

        val boelie2 = Image(
            url = "https://dierenasielgenk.be/wp-content/uploads/2020/11/boelieee-scaled.jpg",
            type = ImageType.PHOTO,
            source = ImageSource.EXTERNAL,
        )
        imageRepository.save(boelie2)

        val boelie = Animal(
            shelter = genk,
            kind = AnimalKind.DOG,
            breed = "Akita",
            name = "Yako",
            description = "Boelie is een actieve hond die graag dingen onderneemt, maar zeker ook nog verdere opvoeding en structuur nodig had.\n" +
                "We zeggen ‘had’, omdat er in het asiel heel hard gewerkt is aan etiquette.\n" +
                "Boelie heeft geleerd om na te denken op momenten dat hij gefrustreerd wordt, daar waar hij voorheen kon happen. Hij heeft hier enorme vooruitgang in geboekt.",
            sex = AnimalSex.MALE,
            birthday = ZonedDateTime.of(2020, 1, 21, 0, 0, 0, 0, ZoneOffset.UTC),
            weight = 34,
            vaccinated = true,
            sterilized = false,
            likes = mutableSetOf(),
            photos = mutableSetOf(boelie1, boelie2)
        )
        animalRepository.save(boelie)

        return "Ok"
    }
}
