package com.pgustavo.chat.domain.exception

class InvalidCredentialsException: RuntimeException(
    "The entered credential aren't valid"
)