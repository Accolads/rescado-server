package org.rescado.server

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.SignatureException
import org.rescado.server.constant.SecurityConstants
import org.rescado.server.service.AccountService
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.servlet.HandlerExceptionResolver
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthorizationFilter(
    authenticationManager: AuthenticationManager,
    private val accountService: AccountService,
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
        ExpiredJwtException::class, // when the JWT is expired
        UnsupportedJwtException::class, // when the JWT is signed with a different key/algorithm
        MalformedJwtException::class, // when the JWT is looking weird
        SignatureException::class, // when the JWT signature is invalid
        JwtException::class, // when authorization header is missing (wrapped IllegalArgumentException)
        AuthenticationCredentialsNotFoundException::class // when the JWT's associated account (no longer) exists
    )
    private fun authenticate(req: HttpServletRequest): UsernamePasswordAuthenticationToken {
        val authHeader = req.getHeader(SecurityConstants.TOKEN_HEADER) ?: throw JwtException("Authorization header is missing")
        if (!authHeader.startsWith(SecurityConstants.TOKEN_PREFIX)) throw UnsupportedJwtException("Authorization header is not a JWT")

        val jwt = Jwts.parserBuilder()
            .setSigningKey(SecurityConstants.JWT_SECRET.toByteArray())
            .build()
            .parseClaimsJws(authHeader.replace(SecurityConstants.TOKEN_PREFIX, ""))

        val account = accountService.getByUuid(jwt.body.subject)
            ?: throw AuthenticationCredentialsNotFoundException("Account does not exist")

        return UsernamePasswordAuthenticationToken(account, null, null)
    }
}
