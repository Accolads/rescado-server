package org.rescado.server.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/session")
class SessionController { // TODO implement ð

    @GetMapping
    fun get() = "Hello there ð"
}
