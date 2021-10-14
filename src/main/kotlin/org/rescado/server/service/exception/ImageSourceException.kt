package org.rescado.server.service.exception

import org.rescado.server.persistence.entity.Image

class ImageSourceException(val type: Image.Type) : RuntimeException()
