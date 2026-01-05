package com.pgustavo.chirp.api.dto

import com.pgustavo.chirp.domain.events.user.type.UserId

data class UserDto(
    val id: UserId,
    val email: String,
    val username: String,
    val hasVerifiedEmail: Boolean,
)
