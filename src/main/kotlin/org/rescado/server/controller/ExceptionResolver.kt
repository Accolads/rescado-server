package org.rescado.server.controller

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.SignatureException
import org.rescado.server.controller.dto.res.Response
import org.rescado.server.controller.dto.res.error.BadRequest
import org.rescado.server.controller.dto.res.error.InternalServerError
import org.rescado.server.controller.dto.res.error.MethodNotAllowed
import org.rescado.server.controller.dto.res.error.NotFound
import org.rescado.server.controller.dto.res.error.Unauthorized
import org.rescado.server.service.MessageService
import org.rescado.server.util.generateResponse
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MissingRequestHeaderException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.NoHandlerFoundException
import javax.servlet.http.HttpServletRequest

@ControllerAdvice
class ExceptionResolver(private val messages: MessageService) {

    @ExceptionHandler
    fun handleHttpMessageNotReadableException(e: HttpMessageNotReadableException): ResponseEntity<Response> {
        return generateResponse(BadRequest(error = messages["rescado.exception.HttpMessageNotReadableException.message"]))
    }

    @ExceptionHandler
    fun handleMissingRequestHeaderException(e: MissingRequestHeaderException): ResponseEntity<Response> {
        return generateResponse(BadRequest(error = messages["rescado.exception.MissingRequestHeaderException.message", e.headerName]))
    }

    @ExceptionHandler
    fun handleNoHandlerFoundException(e: NoHandlerFoundException): ResponseEntity<Response> {
        return generateResponse(NotFound(error = messages["rescado.exception.NoHandlerFoundException.message"]))
    }

    @ExceptionHandler
    fun handleHttpRequestMethodNotSupportedException(e: HttpRequestMethodNotSupportedException): ResponseEntity<Response> {
        return generateResponse(MethodNotAllowed(error = e.message!!))
    }

    @ExceptionHandler
    fun handleExpiredJwtException(e: ExpiredJwtException, req: HttpServletRequest): ResponseEntity<Response> {
        return generateResponse(Unauthorized(reason = Unauthorized.Reason.EXPIRED_ACCESS_TOKEN, realm = req.serverName))
    }

    @ExceptionHandler
    fun handleUnsupportedJwtException(e: UnsupportedJwtException, req: HttpServletRequest): ResponseEntity<Response> {
        return generateResponse(Unauthorized(reason = Unauthorized.Reason.INVALID_ACCESS_TOKEN, realm = req.serverName))
    }

    @ExceptionHandler
    fun handleMalformedJwtException(e: MalformedJwtException, req: HttpServletRequest): ResponseEntity<Response> {
        return generateResponse(Unauthorized(reason = Unauthorized.Reason.INVALID_ACCESS_TOKEN, realm = req.serverName))
    }

    @ExceptionHandler
    fun handleSignatureException(e: SignatureException, req: HttpServletRequest): ResponseEntity<Response> {
        return generateResponse(Unauthorized(reason = Unauthorized.Reason.INVALID_ACCESS_TOKEN, realm = req.serverName))
    }

    @ExceptionHandler
    fun handleJwtException(e: JwtException, req: HttpServletRequest): ResponseEntity<Response> {
        return generateResponse(Unauthorized(reason = Unauthorized.Reason.NO_TOKEN_PROVIDED, realm = req.serverName))
    }

    @ExceptionHandler
    fun handleAuthenticationCredentialsNotFoundException(e: AuthenticationCredentialsNotFoundException, req: HttpServletRequest): ResponseEntity<Response> {
        return generateResponse(Unauthorized(reason = Unauthorized.Reason.INVALID_TOKEN_ACCOUNT, realm = req.serverName))
    }

    @ExceptionHandler
    fun handleException(e: Exception): ResponseEntity<Response> {
        val oopsies = mutableListOf(messages["rescado.exception.Exception.message"], e.message ?: e.javaClass.simpleName)
        var cause = e.cause
        var limit = 10
        while (cause != null && limit != 0) {
            oopsies.add(cause.message ?: cause.javaClass.simpleName)
            cause = cause.cause
            limit--
        }
        return generateResponse(InternalServerError(errors = oopsies.toList()))
    }
}
