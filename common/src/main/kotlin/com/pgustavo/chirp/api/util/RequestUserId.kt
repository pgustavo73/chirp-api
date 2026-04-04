package com.pgustavo.chirp.api.util


import com.pgustavo.chirp.domain.type.UserId
import com.pgustavo.chirp.domain.execption.UnauthorizedException
import org.springframework.security.core.context.SecurityContextHolder

val requestUserId: UserId
    get() = SecurityContextHolder.getContext().authentication?.principal as? UserId
        ?: throw UnauthorizedException()