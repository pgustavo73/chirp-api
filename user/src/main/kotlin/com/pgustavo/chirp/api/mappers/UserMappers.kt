package com.pgustavo.chirp.api.mappers

import com.pgustavo.chirp.api.dto.AuthenticatedUserDto
import com.pgustavo.chirp.api.dto.UserDto
import com.pgustavo.chirp.domain.model.AuthenticatedUser
import com.pgustavo.chirp.domain.model.User

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