package org.rescado.server.service.exception

class PhotoMinimumLimitReachedException(val limit: Int) : RuntimeException()
