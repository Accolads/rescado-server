package org.rescado.server.service.exception

class LastReferenceException(val referenceName: String = "") : IllegalArgumentException("Last reference $referenceName")
