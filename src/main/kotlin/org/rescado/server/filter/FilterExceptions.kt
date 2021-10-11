package org.rescado.server.filter

import java.lang.IllegalArgumentException

open class BasicAuthorizationException(message: String) : IllegalArgumentException(message)

class UnsupportedBasicAuthorizationException(message: String) : BasicAuthorizationException(message)

class MalformedBasicAuthorizationException(message: String) : BasicAuthorizationException(message)
