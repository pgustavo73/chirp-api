package com.pgustavo.chat.api.dto

data class LoginRequest(
    val email: String,
    val password: String,
)
