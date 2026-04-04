package com.pgustavo.chirp.infra.database.mappers

import com.pgustavo.chirp.domain.model.EmailVerificationToken
import com.pgustavo.chirp.infra.database.entities.EmailVerificationTokenEntity

fun EmailVerificationTokenEntity.toEmailVerificationToken(): EmailVerificationToken {
    return EmailVerificationToken(
        id = id,
        token = token,
        user = user.toUser()
    )
}