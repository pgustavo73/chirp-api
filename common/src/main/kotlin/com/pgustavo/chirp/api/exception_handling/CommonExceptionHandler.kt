package com.pgustavo.chirp.api.exception_handling

import com.pgustavo.chirp.domain.execption.ForbiddenException
import com.pgustavo.chirp.domain.execption.UnauthorizedException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus


class CommonExceptionHandler {

    @ExceptionHandler(ForbiddenException::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    fun onForbidden(e: ForbiddenException) = mapOf(
        "code" to "FORBIDDEN",
        "message" to e.message
    )

    @ExceptionHandler(UnauthorizedException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun onUnauthorized(e: UnauthorizedException) = mapOf(
        "code" to "UNAUTHORIZED",
        "message" to e.message
    )
}