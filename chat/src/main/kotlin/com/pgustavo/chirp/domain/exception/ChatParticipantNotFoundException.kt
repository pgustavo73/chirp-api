package com.pgustavo.chirp.domain.exception

import com.pgustavo.chirp.domain.type.UserId

class ChatParticipantNotFoundException(
    id: UserId
): RuntimeException(
    "The chat participant with the ID $id was not found."
)