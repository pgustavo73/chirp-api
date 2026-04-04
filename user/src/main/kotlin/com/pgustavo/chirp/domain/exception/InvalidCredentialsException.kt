package com.pgustavo.chirp.domain.exception

class InvalidCredentialsException: RuntimeException(
    "The entered credential aren't valid"
)