package org.rescado.server.filter

import org.postgresql.util.Base64
import org.rescado.server.constant.SecurityConstants
import org.rescado.server.persistence.entity.Admin
import org.rescado.server.service.AdminService
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.servlet.HandlerExceptionResolver
import java.lang.IllegalArgumentException
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class BasicAuthorizationFilter(
    authenticationManager: AuthenticationManager,
    private val adminService: AdminService,
    private val handlerExceptionResolver: HandlerExceptionResolver
) : BasicAuthenticationFilter(authenticationManager) {

    override fun doFilterInternal(req: HttpServletRequest, res: HttpServletResponse, chain: FilterChain) {
        try {
            SecurityContextHolder.getContext().authentication = this.authenticate(req)
            chain.doFilter(req, res)
        } catch (e: Exception) {
            handlerExceptionResolver.resolveException(req, res, null, e)
            return
        }
    }

    // Throws exceptions if authentication fails
    @Throws(
        BasicAuthorizationException::class, // when authorization header is missing (wrapped IllegalArgumentException)
        UnsupportedBasicAuthorizationException::class, // when the authorization header is not a basic authentication value
        MalformedBasicAuthorizationException::class, // when the authorization header's value is not a base64 encoded credentials string
    )
    private fun authenticate(req: HttpServletRequest): UsernamePasswordAuthenticationToken {
        val authHeader = req.getHeader(SecurityConstants.AUTHORIZATION_HEADER) ?: throw BasicAuthorizationException("Authorization header is missing")
        if (!authHeader.startsWith(SecurityConstants.BASIC_PREFIX)) throw UnsupportedBasicAuthorizationException("Authorization header is not a basic authentication header")

        val admin: Admin?
        try {
            val credentials = Base64.decode(authHeader.replace(SecurityConstants.BASIC_PREFIX, "")).toString()
            admin = adminService.getByUsernameAndPassword(credentials.substringBefore(":"), credentials.substringAfter(":"))
        } catch (e: Exception) {
            throw MalformedBasicAuthorizationException("Authorization header is malformed")
        }

        admin ?: throw AuthenticationCredentialsNotFoundException("Admin does not exist")

        return UsernamePasswordAuthenticationToken(admin, null, null)
    }
}

open class BasicAuthorizationException(message: String) : IllegalArgumentException(message)

class UnsupportedBasicAuthorizationException(message: String) : BasicAuthorizationException(message)

class MalformedBasicAuthorizationException(message: String) : BasicAuthorizationException(message)
