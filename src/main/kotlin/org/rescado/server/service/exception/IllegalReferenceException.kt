package org.rescado.server.service.exception

class IllegalReferenceException(val referenceName: String) : IllegalArgumentException("Illegal reference $referenceName")
