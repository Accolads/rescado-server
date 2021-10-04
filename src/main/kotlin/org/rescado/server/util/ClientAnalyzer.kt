package org.rescado.server.util

import nl.basjes.parse.useragent.UserAgentAnalyzer
import org.springframework.stereotype.Component

private const val UNKNOWN_AGENT = "Unidentified browser"
private const val UNKNOWN_OS = "an unknown device"

@Component
class ClientAnalyzer {

    private val userAgentAnalyzer = UserAgentAnalyzer
        .newBuilder()
        .hideMatcherLoadStats()
        .withCache(10000)
        .build()

    fun getFromUserAgent(userAgent: String): String {
        return when {
            userAgent.contains("rescado", true) -> userAgent
            userAgent.contains("postman", true) -> userAgent
            userAgent.contains("insomnia", true) -> userAgent
            else -> {
                val analyzer = userAgentAnalyzer.parse(userAgent)

                val agent = analyzer.getValue("AgentNameVersionMajor").let {
                    if (it.equals("??")) return UNKNOWN_AGENT
                }
                val os = analyzer.getValue("OperatingSystemNameVersionMajor").let {
                    if (it.equals("??")) return UNKNOWN_OS
                }
                return "$agent on $os"
            }
        }
    }
}
