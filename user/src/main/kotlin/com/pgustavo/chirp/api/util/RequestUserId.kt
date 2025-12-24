package com.pgustavo.chirp.api.util

import com.pgustavo.chirp.domain.exception.UnauthorizedException
import com.pgustavo.chirp.domain.model.UserId
import org.springframework.security.core.context.SecurityContextHolder

val requestUserId: UserId
    get() = SecurityContextHolder.getContext().authentication?.principal as? UserId
        ?: throw UnauthorizedException()