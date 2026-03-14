package com.pgustavo.chat.infra.database.repositories

import com.pgustavo.chat.domain.events.user.type.UserId
import com.pgustavo.chat.infra.database.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<UserEntity, UserId> {
    fun  findByEmail(email: String): UserEntity?

    fun findByEmailOrUsername(email: String, username: String): UserEntity?

}