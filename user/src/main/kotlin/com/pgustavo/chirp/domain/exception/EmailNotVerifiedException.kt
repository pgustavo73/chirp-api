package com.pgustavo.chirp.domain.exception

import java.lang.RuntimeException

class EmailNotVerifiedException: RuntimeException(
    "Email not verified"
)