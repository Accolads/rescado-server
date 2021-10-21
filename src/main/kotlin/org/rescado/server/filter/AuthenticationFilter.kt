package org.rescado.server.filter

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.security.SignatureException
import org.postgresql.util.Base64
import org.rescado.server.constant.SecurityConstants
import org.rescado.server.filter.exception.MalformedCredentialsException
import org.rescado.server.filter.exception.MissingCredentialsException
import org.rescado.server.filter.exception.UnsupportedCredentialsException
import org.rescado.server.persistence.entity.Admin
import org.rescado.server.service.AccountService
import org.rescado.server.service.AdminService
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.servlet.HandlerExceptionResolver
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthenticationFilter(
    authenticationManager: AuthenticationManager,
    private val adminService: AdminService,
    private val accountService: AccountService,
    private val handlerExceptionResolver: HandlerExceptionResolver
) : BasicAuthenticationFilter(authenticationManager) {

    @Throws(
        MissingCredentialsException::class, // when there is no Authorization header present on the request
        UnsupportedCredentialsException::class, // when the Authorization header contains something different from bearer or basic token
    )
    override fun doFilterInternal(req: HttpServletRequest, res: HttpServletResponse, chain: FilterChain) {
        try {
            val authHeader = req.getHeader(SecurityConstants.AUTHORIZATION_HEADER) ?: throw MissingCredentialsException()

            SecurityContextHolder.getContext().authentication = when {
                authHeader.startsWith(SecurityConstants.TOKEN_PREFIX) -> bearerAuthentication(authHeader)
                authHeader.startsWith(SecurityConstants.BASIC_PREFIX) -> basicAuthentication(authHeader)
                else -> throw UnsupportedCredentialsException()
            }
            chain.doFilter(req, res)
        } catch (e: Exception) {
            handlerExceptionResolver.resolveException(req, res, null, e)
            return
        }
    }

    @Throws(
        AuthenticationCredentialsNotFoundException::class, // when the JWT's associated account does not exist or credentials mismatch
        MalformedJwtException::class, // when the authorization header's value is not a valid JWT token
        SignatureException::class, // when the JWT signature is invalid
        ExpiredJwtException::class, // when the JWT is expired
    )
    private fun bearerAuthentication(authHeader: String): UsernamePasswordAuthenticationToken {
        val jwt = Jwts.parserBuilder()
            .setSigningKey(SecurityConstants.JWT_SECRET.toByteArray())
            .build()
            .parseClaimsJws(authHeader.replace(SecurityConstants.TOKEN_PREFIX, ""))

        val account = accountService.getByUuid(jwt.body.subject)
            ?: throw AuthenticationCredentialsNotFoundException("Account does not exist")

        return UsernamePasswordAuthenticationToken(
            account,
            authHeader,
            listOf(SimpleGrantedAuthority(account::class.simpleName))
        )
    }

    @Throws(
        AuthenticationCredentialsNotFoundException::class, // when the credentials mismatch or don't belong to any admin
        MalformedCredentialsException::class, // when the authorization header's value is not a base64 encoded credentials string
    )
    private fun basicAuthentication(authHeader: String): UsernamePasswordAuthenticationToken {
        val admin: Admin?
        try {
            val credentials = String(Base64.decode(authHeader.replace(SecurityConstants.BASIC_PREFIX, "")))
            admin = adminService.getByUsernameAndPassword(credentials.substringBefore(":"), credentials.substringAfter(":"))
        } catch (e: Exception) {
            throw MalformedCredentialsException()
        }

        admin ?: throw AuthenticationCredentialsNotFoundException("Admin does not exist")

        return UsernamePasswordAuthenticationToken(
            admin,
            authHeader,
            listOf(SimpleGrantedAuthority(admin::class.simpleName))
        )
    }
}
