package com.pgustavo.chirp.api.dto.ws

import com.pgustavo.chirp.domain.type.ChatId

data class ChatParticipantsChangedDto(
    val chatId: ChatId,
)
