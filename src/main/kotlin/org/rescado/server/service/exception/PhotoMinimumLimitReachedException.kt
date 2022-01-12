package org.rescado.server.service.exception

class PhotoMinimumLimitReachedException(val limit: Int) : RuntimeException("Minimum limit of $limit reached")
