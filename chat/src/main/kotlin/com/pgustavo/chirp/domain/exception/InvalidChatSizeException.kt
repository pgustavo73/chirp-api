package com.pgustavo.chirp.domain.exception

class InvalidChatSizeException: RuntimeException(
    "There must to be at least 2 unique participants to create a chat."
)