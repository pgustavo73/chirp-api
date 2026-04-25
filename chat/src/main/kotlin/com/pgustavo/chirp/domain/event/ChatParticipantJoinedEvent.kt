package com.pgustavo.chirp.domain.event

import com.pgustavo.chirp.domain.type.ChatId
import com.pgustavo.chirp.domain.type.UserId

data class ChatParticipantJoinedEvent(
    val chatId: ChatId,
    val userId: Set<UserId>,
)
