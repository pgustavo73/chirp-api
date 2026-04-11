package com.pgustavo.chirp.api.execption_hadling

import com.pgustavo.chirp.domain.exception.ChatNotFoundException
import com.pgustavo.chirp.domain.exception.ChatParticipantNotFoundException
import com.pgustavo.chirp.domain.exception.InvalidChatSizeException
import com.pgustavo.chirp.domain.exception.MessageNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

class ChatExceptionHandler {

    @ExceptionHandler(
        ChatNotFoundException::class,
        MessageNotFoundException::class,
        ChatParticipantNotFoundException::class,
    )
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun onForbidden(e: Exception) = mapOf(
        "code" to "FORBIDDEN",
        "message" to e.message
    )

    @ExceptionHandler(InvalidChatSizeException::class,)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun onForbidden(e: InvalidChatSizeException) = mapOf(
        "code" to "INVALID_CHAT_SIZE",
        "message" to e.message
    )
}