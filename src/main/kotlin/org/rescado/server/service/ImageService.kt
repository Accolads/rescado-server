package org.rescado.server.service

import org.rescado.server.constant.SecurityConstants
import org.rescado.server.persistence.entity.Image
import org.rescado.server.persistence.repository.ImageRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import javax.transaction.Transactional

@Service
@Transactional
class ImageService(
    private val imageRepository: ImageRepository,
) {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    fun getById(id: Long) = imageRepository.getById(id)

    @Throws(ImageSourceException::class)
    fun create(type: Image.Type, reference: String): Image {
        val image = Image(
            reference = reference,
            type = type,
            source = when {
                isValidInternalReference(reference) -> Image.Source.INTERNAL
                isValidExternalReference(reference) -> Image.Source.EXTERNAL
                else -> throw ImageSourceException(type)
            }
        )
        return imageRepository.save(image)
    }

    private fun isValidInternalReference(reference: String) = reference.startsWith("rescado/")

    private fun isValidExternalReference(reference: String): Boolean {
        var contentType = ""
        return try {
            val url = URL(reference)
            val con = url.openConnection()

            contentType = con.contentType
            SecurityConstants.IMAGE_CONTENTTYPE_WHITELIST.first {
                contentType.contains(it, true)
            }.isNotBlank()
        } catch (e: Exception) {
            when (e) {
                is MalformedURLException -> logger.warn("The reference is a malformed URL")
                is IOException -> logger.warn("Can't open a connection to the reference")
                is NoSuchElementException -> logger.warn("The reference is of type \"$contentType\" which is not allowed")
                else -> logger.warn("Something strange just happened")
            }
            false
        }
    }
}