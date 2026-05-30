package com.pgustavo.chirp.api.dto.ws

import com.pgustavo.chirp.domain.type.ChatId
import com.pgustavo.chirp.domain.type.ChatMessageId

data class DeleteMessageDto(
    val chatId: ChatId,
    val messageId: ChatMessageId,
)
