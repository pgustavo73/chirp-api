package com.pgustavo.chat.domain.models

import com.pgustavo.chat.domain.events.user.type.UserId

data class ChatParticipant (
    val userId: UserId,
    val username: String,
    val email: String,
    val profilePictureUrl: String?,
)