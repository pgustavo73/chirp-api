package com.pgustavo.chirp.service.auth

import com.pgustavo.chirp.domain.exception.UserAlreadyExistsException
import com.pgustavo.chirp.domain.model.User
import com.pgustavo.chirp.infra.database.entities.UserEntity
import com.pgustavo.chirp.infra.database.mappers.toUser
import com.pgustavo.chirp.infra.database.repositories.UserRepository
import com.pgustavo.chirp.infra.security.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {

    fun register(email: String, username: String, password: String): User {
        val user = userRepository.findByEmailOrUsername(
            email = email.trim(),
            username = username.trim(),
        )
        if (user != null) {
            throw UserAlreadyExistsException()
        }
        val saveUser = userRepository.save(
            UserEntity(
                email = email.trim(),
                username = username.trim(),
                hashedPassword = passwordEncoder.encode(password)
            )
        ).toUser()
        return saveUser
    }
}