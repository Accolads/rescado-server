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

    fun analyze(device: String?, agent: String): Pair<String, String> {
        if (device.isNullOrBlank()) {
            val analyzer = userAgentAnalyzer.parse(agent)
            return Pair(sanitize(analyzer.getValue("OperatingSystemNameVersionMajor")), sanitize(analyzer.getValue("AgentNameVersionMajor")))
        }
        return Pair(device, agent)
    }

    private fun sanitize(input: String) = input.replace('?', ' ').trim().ifBlank { "Unknown" }
}
