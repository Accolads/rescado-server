package org.rescado.server.service.exception

class PhotoMaximumLimitReachedException(val limit: Int) : RuntimeException()
