package com.pgustavo.chirp.api.dto

import com.pgustavo.chirp.domain.type.UserId
import com.pgustavo.chirp.domain.type.ChatId
import com.pgustavo.chirp.domain.type.ChatMessageId
import java.time.Instant

data class ChatMessageDto(
    val id: ChatMessageId,
    val chatId: ChatId,
    val content: String,
    val createdAt: Instant,
    val senderId: UserId,
)
