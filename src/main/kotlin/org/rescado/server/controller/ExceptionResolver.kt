package org.rescado.server.controller

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.SignatureException
import org.rescado.server.controller.dto.build
import org.rescado.server.controller.dto.res.Response
import org.rescado.server.controller.dto.res.error.BadRequest
import org.rescado.server.controller.dto.res.error.InternalServerError
import org.rescado.server.controller.dto.res.error.MethodNotAllowed
import org.rescado.server.controller.dto.res.error.NotFound
import org.rescado.server.controller.dto.res.error.Unauthorized
import org.rescado.server.filter.BasicAuthorizationException
import org.rescado.server.filter.MalformedBasicAuthorizationException
import org.rescado.server.filter.UnsupportedBasicAuthorizationException
import org.rescado.server.service.ImageSourceException
import org.rescado.server.service.MessageService
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
    fun handleImageSourceException(e: ImageSourceException) =
        BadRequest(error = messages["exception.ImageSourceException.message", e.type.name.lowercase()]).build()

    @ExceptionHandler
    fun handleHttpMessageNotReadableException(e: HttpMessageNotReadableException) =
        BadRequest(error = messages["exception.HttpMessageNotReadableException.message"]).build()

    @ExceptionHandler
    fun handleMissingRequestHeaderException(e: MissingRequestHeaderException) =
        BadRequest(error = messages["exception.MissingRequestHeaderException.message", e.headerName]).build()

    @ExceptionHandler
    fun handleNoHandlerFoundException(e: NoHandlerFoundException) =
        NotFound(error = messages["exception.NoHandlerFoundException.message"]).build()

    @ExceptionHandler
    fun handleHttpRequestMethodNotSupportedException(e: HttpRequestMethodNotSupportedException) =
        MethodNotAllowed(error = e.message!!).build()

    @ExceptionHandler
    fun handleJwtException(e: JwtException, req: HttpServletRequest) =
        Unauthorized(reason = Unauthorized.Reason.NO_CREDENTIALS, realm = req.serverName).build()

    @ExceptionHandler
    fun handleBasicAuthorizationException(e: BasicAuthorizationException, req: HttpServletRequest) =
        Unauthorized(reason = Unauthorized.Reason.NO_CREDENTIALS, realm = req.serverName).build()

    @ExceptionHandler
    fun handleUnsupportedJwtException(e: UnsupportedJwtException, req: HttpServletRequest) =
        Unauthorized(reason = Unauthorized.Reason.INVALID_CREDENTIALS, realm = req.serverName).build()

    @ExceptionHandler
    fun handleUnsupportedBasicAuthorizationException(e: UnsupportedBasicAuthorizationException, req: HttpServletRequest) =
        Unauthorized(reason = Unauthorized.Reason.INVALID_CREDENTIALS, realm = req.serverName).build()

    @ExceptionHandler
    fun handleMalformedJwtException(e: MalformedJwtException, req: HttpServletRequest) =
        Unauthorized(reason = Unauthorized.Reason.MALFORMED_CREDENTIALS, realm = req.serverName).build()

    @ExceptionHandler
    fun handleMalformedBasicAuthorizationException(e: MalformedBasicAuthorizationException, req: HttpServletRequest) =
        Unauthorized(reason = Unauthorized.Reason.MALFORMED_CREDENTIALS, realm = req.serverName).build()

    @ExceptionHandler
    fun handleSignatureException(e: SignatureException, req: HttpServletRequest) =
        Unauthorized(reason = Unauthorized.Reason.INVALID_ACCESS_TOKEN, realm = req.serverName).build()

    @ExceptionHandler
    fun handleExpiredJwtException(e: ExpiredJwtException, req: HttpServletRequest) =
        Unauthorized(reason = Unauthorized.Reason.EXPIRED_ACCESS_TOKEN, realm = req.serverName).build()

    @ExceptionHandler
    fun handleAuthenticationCredentialsNotFoundException(e: AuthenticationCredentialsNotFoundException, req: HttpServletRequest) =
        Unauthorized(reason = Unauthorized.Reason.INVALID_CREDENTIALS, realm = req.serverName).build()

    @ExceptionHandler
    fun handleException(e: Exception): ResponseEntity<Response> {
        val oopsies = mutableListOf(messages["exception.Exception.message"], e.message ?: e.javaClass.simpleName)
        var cause = e.cause
        var limit = 10
        while (cause != null && limit != 0) {
            oopsies.add(cause.message ?: cause.javaClass.simpleName)
            cause = cause.cause
            limit--
        }
        return InternalServerError(errors = oopsies.toList()).build()
    }
}
