package com.pgustavo.chirp.domain.exception

import jdk.internal.joptsimple.internal.Messages.message

class InvalidCredentialsException: RuntimeException(
    "The entered credential aren't valid"
)