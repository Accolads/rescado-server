package org.rescado.server.service.exception

class PhotoMaximumLimitReachedException(val limit: Int) : RuntimeException("Maximum limit of $limit reached")
