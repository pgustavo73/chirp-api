package com.pgustavo.chirp.domain.model

import com.pgustavo.chirp.domain.events.user.type.UserId



data class User(
    val id: UserId,
    val username: String,
    val email: String,
    val hasEmailVerified: Boolean,
)
