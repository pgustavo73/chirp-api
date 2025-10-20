package com.pgustavo.chirp

import com.pgustavo.chat.Test
import com.pgustavo.chirp.infra.database.entities.UserEntity
import com.pgustavo.chirp.infra.database.repositories.UserRepository
import jakarta.annotation.PostConstruct
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component

@SpringBootApplication
class ChirpApplication

fun main(args: Array<String>) {
	runApplication<ChirpApplication>(*args)
}

@Component
class Demo(
    private val repository: UserRepository
) {
    @PostConstruct
    fun int() {
        repository.save(
            UserEntity(
                email = "gus@test.com",
                username = "myTest",
                hashedPassword = "1234"
            )
        )
    }
}
