package com.pgustavo.chirp.api.dto.ws

import com.pgustavo.chirp.domain.type.ChatId
import com.pgustavo.chirp.domain.type.ChatMessageId

data class SendMessageDto (
    val chatId: ChatId,
    val content: String,
    val messageId: ChatMessageId? = null,
)