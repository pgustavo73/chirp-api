package com.pgustavo.chirp.domain.models

import com.pgustavo.chirp.domain.type.UserId

data class ChatParticipant (
    val userId: UserId,
    val username: String,
    val email: String,
    val profilePictureUrl: String?,
)