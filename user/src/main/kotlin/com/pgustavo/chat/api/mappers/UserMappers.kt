package com.pgustavo.chat.api.mappers

import com.pgustavo.chat.api.dto.AuthenticatedUserDto
import com.pgustavo.chat.api.dto.UserDto
import com.pgustavo.chat.domain.model.AuthenticatedUser
import com.pgustavo.chat.domain.model.User

fun AuthenticatedUser.toAuthenticatedUserDto(): AuthenticatedUserDto{
    return AuthenticatedUserDto(
        user = user.toUserDto(),
        accessToken = accessToken,
        refreshToken = refreshToken,
    )
}

fun User.toUserDto(): UserDto {
    return UserDto(
        id = id,
        email = email,
        username = username,
        hasVerifiedEmail = hasEmailVerified,
    )
}