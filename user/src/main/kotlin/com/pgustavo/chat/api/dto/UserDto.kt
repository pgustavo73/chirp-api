package com.pgustavo.chat.api.dto

import com.pgustavo.chat.domain.events.user.type.UserId

data class UserDto(
    val id: UserId,
    val email: String,
    val username: String,
    val hasVerifiedEmail: Boolean,
)
