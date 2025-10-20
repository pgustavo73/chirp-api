package com.pgustavo.chirp.api.dto

import com.pgustavo.chirp.domain.model.UserID

data class UserDto(
    val id: UserID,
    val email: String,
    val username: String,
    val hasVerifiedEmail: Boolean,
)
