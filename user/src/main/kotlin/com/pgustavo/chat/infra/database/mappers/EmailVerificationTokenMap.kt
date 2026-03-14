package com.pgustavo.chat.infra.database.mappers

import com.pgustavo.chat.domain.model.EmailVerificationToken
import com.pgustavo.chat.infra.database.entities.EmailVerificationTokenEntity

fun EmailVerificationTokenEntity.toEmailVerificationToken(): EmailVerificationToken {
    return EmailVerificationToken(
        id = id,
        token = token,
        user = user.toUser()
    )
}