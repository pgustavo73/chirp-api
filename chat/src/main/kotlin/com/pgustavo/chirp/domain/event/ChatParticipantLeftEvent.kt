package com.pgustavo.chirp.domain.event

import com.pgustavo.chirp.domain.type.ChatId
import com.pgustavo.chirp.domain.type.UserId

data class ChatParticipantLeftEvent(
    val chatId: ChatId,
    val userId: UserId,
)
