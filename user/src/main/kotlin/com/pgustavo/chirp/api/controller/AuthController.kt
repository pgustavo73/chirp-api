package com.pgustavo.chirp.api.controller

import com.pgustavo.chirp.api.dto.RegisterRequest
import com.pgustavo.chirp.api.dto.UserDto
import com.pgustavo.chirp.api.mappers.toUserDto
import com.pgustavo.chirp.service.auth.AuthService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(private val authService: AuthService) {

    @PostMapping("/register")
    fun register(
        @Valid @RequestBody body: RegisterRequest
    ): UserDto {
        return authService.register(
            email = body.email,
            username = body.username,
            password = body.password,
        ).toUserDto()
    }
}