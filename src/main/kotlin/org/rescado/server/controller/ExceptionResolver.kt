package org.rescado.server.controller

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.security.SignatureException
import org.rescado.server.controller.dto.build
import org.rescado.server.controller.dto.res.Response
import org.rescado.server.controller.dto.res.error.BadRequest
import org.rescado.server.controller.dto.res.error.InternalServerError
import org.rescado.server.controller.dto.res.error.MethodNotAllowed
import org.rescado.server.controller.dto.res.error.NotFound
import org.rescado.server.controller.dto.res.error.Unauthorized
import org.rescado.server.filter.exception.MalformedCredentialsException
import org.rescado.server.filter.exception.MissingCredentialsException
import org.rescado.server.filter.exception.UnsupportedCredentialsException
import org.rescado.server.service.MessageService
import org.rescado.server.service.exception.ImageSourceException
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
class ExceptionResolver(
    private val messageService: MessageService,
) {

    @ExceptionHandler
    fun handleImageSourceException(e: ImageSourceException) =
        BadRequest(error = messageService["exception.ImageSourceException.message", e.type.name.lowercase()]).build()

    @ExceptionHandler
    fun handleHttpMessageNotReadableException(e: HttpMessageNotReadableException) =
        BadRequest(error = messageService["exception.HttpMessageNotReadableException.message"]).build()

    @ExceptionHandler
    fun handleMissingRequestHeaderException(e: MissingRequestHeaderException) =
        BadRequest(error = messageService["exception.MissingRequestHeaderException.message", e.headerName]).build()

    @ExceptionHandler
    fun handleNoHandlerFoundException(e: NoHandlerFoundException) =
        NotFound(error = messageService["exception.NoHandlerFoundException.message"]).build()

    @ExceptionHandler
    fun handleHttpRequestMethodNotSupportedException(e: HttpRequestMethodNotSupportedException) =
        MethodNotAllowed(error = messageService["exception.HttpRequestMethodNotSupportedException.message", e.method]).build()

    @ExceptionHandler
    fun handleAuthenticationCredentialsNotFoundException(e: AuthenticationCredentialsNotFoundException, req: HttpServletRequest) =
        Unauthorized(error = messageService["exception.AuthenticationCredentialsNotFoundException.message"], reason = Unauthorized.Reason.INVALID_CREDENTIALS, realm = req.serverName).build()

    @ExceptionHandler
    fun handleMissingCredentialsException(e: MissingCredentialsException, req: HttpServletRequest) =
        Unauthorized(error = messageService["exception.MissingCredentialsException.message"], reason = Unauthorized.Reason.NO_CREDENTIALS, realm = req.serverName).build()

    @ExceptionHandler
    fun handleUnsupportedCredentialsException(e: UnsupportedCredentialsException, req: HttpServletRequest) =
        Unauthorized(error = messageService["exception.UnsupportedCredentialsException.message"], reason = Unauthorized.Reason.INVALID_CREDENTIALS, realm = req.serverName).build()

    @ExceptionHandler
    fun handleMalformedCredentialsException(e: MalformedCredentialsException, req: HttpServletRequest) =
        Unauthorized(error = messageService["exception.MalformedCredentialsException.message"], reason = Unauthorized.Reason.MALFORMED_CREDENTIALS, realm = req.serverName).build()

    @ExceptionHandler
    fun handleMalformedJwtException(e: MalformedJwtException, req: HttpServletRequest) =
        Unauthorized(error = messageService["exception.MalformedJwtException.message"], reason = Unauthorized.Reason.MALFORMED_CREDENTIALS, realm = req.serverName).build()

    @ExceptionHandler
    fun handleSignatureException(e: SignatureException, req: HttpServletRequest) =
        Unauthorized(error = messageService["exception.SignatureException.message"], reason = Unauthorized.Reason.INVALID_ACCESS_TOKEN, realm = req.serverName).build()

    @ExceptionHandler
    fun handleExpiredJwtException(e: ExpiredJwtException, req: HttpServletRequest) =
        Unauthorized(error = messageService["exception.ExpiredJwtException.message"], reason = Unauthorized.Reason.EXPIRED_ACCESS_TOKEN, realm = req.serverName).build()

    @ExceptionHandler
    fun handleException(e: Exception): ResponseEntity<Response> {
        val oopsies = mutableListOf(messageService["exception.Exception.message"], e.message ?: e.javaClass.simpleName)
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
