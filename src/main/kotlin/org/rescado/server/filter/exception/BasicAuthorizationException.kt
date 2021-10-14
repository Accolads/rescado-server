package org.rescado.server.filter.exception

import java.lang.IllegalArgumentException

open class BasicAuthorizationException(message: String) : IllegalArgumentException(message)
