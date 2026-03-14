package com.pgustavo.chat.infra.database.mappers

import com.pgustavo.chat.domain.model.User
import com.pgustavo.chat.infra.database.entities.UserEntity

fun UserEntity.toUser(): User {
    return User(
        id = id!!,
        username = username,
        email = email,
        hasEmailVerified = hasVerifiedEmail,
    )
}