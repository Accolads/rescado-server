package org.rescado.server.controller

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.security.SignatureException
import org.rescado.server.controller.dto.build
import org.rescado.server.controller.dto.res.error.BadRequest
import org.rescado.server.controller.dto.res.error.Conflict
import org.rescado.server.controller.dto.res.error.InternalServerError
import org.rescado.server.controller.dto.res.error.MethodNotAllowed
import org.rescado.server.controller.dto.res.error.NotFound
import org.rescado.server.controller.dto.res.error.Unauthorized
import org.rescado.server.filter.exception.MalformedCredentialsException
import org.rescado.server.filter.exception.MissingCredentialsException
import org.rescado.server.filter.exception.UnsupportedCredentialsException
import org.rescado.server.service.MessageService
import org.rescado.server.service.exception.IllegalReferenceException
import org.rescado.server.service.exception.ImageSourceException
import org.rescado.server.service.exception.LastReferenceException
import org.rescado.server.service.exception.PhotoMaximumLimitReachedException
import org.rescado.server.service.exception.PhotoMinimumLimitReachedException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.web.firewall.RequestRejectedException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MissingRequestHeaderException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.NoHandlerFoundException
import javax.servlet.http.HttpServletRequest

@ControllerAdvice
class ExceptionResolver(
    private val messageService: MessageService,
) {

    @ExceptionHandler
    fun handleIllegalReferenceException(e: IllegalReferenceException) =
        BadRequest(error = messageService["exception.IllegalReferenceException.message", e.referenceName]).build()

    @ExceptionHandler
    fun handleLastReferenceException(e: LastReferenceException) =
        BadRequest(error = messageService["exception.LastReferenceException.message", e.referenceName]).build()

    @ExceptionHandler
    fun handleImageSourceException(e: ImageSourceException) =
        BadRequest(error = messageService["exception.ImageSourceException.message", e.type.name.lowercase()]).build()

    @ExceptionHandler
    fun handlePhotoMinimumLimitReachedException(e: PhotoMinimumLimitReachedException) =
        Conflict(error = messageService["exception.PhotoMinimumLimitReachedException.message", e.limit]).build()

    @ExceptionHandler
    fun handlePhotoMaximumLimitReachedException(e: PhotoMaximumLimitReachedException) =
        Conflict(error = messageService["exception.PhotoMaximumLimitReachedException.message", e.limit]).build()

    @ExceptionHandler
    fun handleHttpMessageNotReadableException(e: HttpMessageNotReadableException) =
        BadRequest(error = messageService["exception.HttpMessageNotReadableException.message"]).build()

    @ExceptionHandler
    fun handleHttpMediaTypeNotSupportedException(e: HttpMediaTypeNotSupportedException) =
        BadRequest(error = messageService["exception.HttpMediaTypeNotSupportedException.message", e.contentType.toString()]).build()

    @ExceptionHandler
    fun handleMissingRequestHeaderException(e: MissingRequestHeaderException) =
        BadRequest(error = messageService["exception.MissingRequestHeaderException.message", e.headerName]).build()

    @ExceptionHandler
    fun handleNoHandlerFoundException(e: NoHandlerFoundException) =
        NotFound(error = messageService["exception.NoHandlerFoundException.message"]).build()

    @ExceptionHandler
    fun handleMethodArgumentTypeMismatchException(e: MethodArgumentTypeMismatchException) =
        BadRequest(error = messageService["exception.MethodArgumentTypeMismatchException.message", e.value]).build()

    @ExceptionHandler
    fun handleHttpRequestMethodNotSupportedException(e: HttpRequestMethodNotSupportedException) =
        MethodNotAllowed(error = messageService["exception.HttpRequestMethodNotSupportedException.message", e.method]).build()

    @ExceptionHandler
    fun handleRequestRejectedException(e: RequestRejectedException) =
        BadRequest(error = messageService["exception.RequestRejectedException.message"]).build()

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
    fun handleDataIntegrityViolationException(e: DataIntegrityViolationException) =
        InternalServerError(error = messageService["exception.DataIntegrityViolationException.message"]).build()

    @ExceptionHandler
    fun handleException(e: Exception) =
        InternalServerError(
            errors = mutableListOf(messageService["exception.Exception.message"]).apply {
                e.printStackTrace()
                var cause: Throwable? = e
                var limit = 10
                while (cause != null && limit != 0) {
                    add("[exception] ${cause.javaClass.simpleName}: ${cause.message}")
                    cause = cause.cause
                    limit--
                }
            }
        ).build()
}
