package org.rescado.server.service.exception

class ImageLimitReachedException(val limit: Int) : RuntimeException()
