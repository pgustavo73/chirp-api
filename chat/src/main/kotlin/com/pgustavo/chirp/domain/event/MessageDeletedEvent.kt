package com.pgustavo.chirp.domain.event

import com.pgustavo.chirp.domain.type.ChatId
import com.pgustavo.chirp.domain.type.ChatMessageId

data class MessageDeletedEvent (
    val chatId: ChatId,
    val messageId: ChatMessageId,
)