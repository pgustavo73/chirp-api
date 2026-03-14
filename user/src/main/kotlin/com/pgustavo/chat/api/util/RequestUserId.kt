package com.pgustavo.chat.api.util

import com.pgustavo.chat.domain.exception.UnauthorizedException
import com.pgustavo.chat.domain.events.user.type.UserId
import org.springframework.security.core.context.SecurityContextHolder

val requestUserId: UserId
    get() = SecurityContextHolder.getContext().authentication?.principal as? UserId
        ?: throw UnauthorizedException()