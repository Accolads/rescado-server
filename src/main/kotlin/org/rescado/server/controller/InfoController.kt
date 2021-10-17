package org.rescado.server.controller

import org.rescado.server.constant.SecurityConstants
import org.rescado.server.controller.dto.build
import org.rescado.server.controller.dto.res.InfoDTO
import org.rescado.server.controller.dto.res.Response
import org.rescado.server.service.SessionService
import org.springframework.boot.info.BuildProperties
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.ZonedDateTime

@RestController
@RequestMapping("${SecurityConstants.PUBLIC_ROUTE}/info")
class InfoController(
    private val sessionService: SessionService,
    private val buildProperties: BuildProperties,
) {

    @GetMapping
    fun getInfo(): ResponseEntity<Response> {
        return InfoDTO(
            name = "rescado-${buildProperties.name}",
            version = buildProperties.version,
            build = buildProperties.time,
            clock = ZonedDateTime.now(),
            sessions = sessionService.getNumberOfActiveSessions(),
        ).build()
    }
}
