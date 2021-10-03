package org.rescado.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RescadoServerApplication

fun main(args: Array<String>) {
    runApplication<RescadoServerApplication>(*args)
}
