package org.rescado.server.util

import nl.basjes.parse.useragent.UserAgentAnalyzer
import org.springframework.stereotype.Component

@Component
class ClientAnalyzer {
    private val userAgentAnalyzer = UserAgentAnalyzer
        .newBuilder()
        .hideMatcherLoadStats()
        .withCache(10000)
        .build()

    fun getFromUserAgent(userAgent: String): String {
        val agent = userAgentAnalyzer.parse(userAgent)

        val browser = agent.getValue("Agent Name Version Major") ?: "Unknown browser"
        val os = agent.getValue("Operating System Name Version Major") ?: "an unknown device"

        return "$browser on $os"
    }
}
