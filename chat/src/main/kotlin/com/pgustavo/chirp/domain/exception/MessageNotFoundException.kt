package com.pgustavo.chirp.domain.exception

import com.pgustavo.chirp.domain.models.ChatMessageId

class MessageNotFoundException(
    private val id: ChatMessageId
): RuntimeException(
    "Message with ID $id not found"
)