package com.pgustavo.chat.domain.model

import com.pgustavo.chat.domain.events.user.type.UserId



data class User(
    val id: UserId,
    val username: String,
    val email: String,
    val hasEmailVerified: Boolean,
)
