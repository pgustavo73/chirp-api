package com.pgustavo.chirp.domain.exception

class SamePasswordException: RuntimeException(
    "The new password can`t be equal to the old one."
)