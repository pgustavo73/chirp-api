package com.pgustavo.chirp.domain.execption

class InvalidTokenException(
    override val message: String?
): RuntimeException(
    message ?: "Invalid token"
)