package com.pgustavo.chirp.infra.database.mappers

import com.pgustavo.chirp.domain.model.User
import com.pgustavo.chirp.infra.database.entities.UserEntity

fun UserEntity.toUser(): User {
    return User(
        id = id!!,
        username = username,
        email = email,
        hasEmailVerified = hasVerifiedEmail,
    )
}