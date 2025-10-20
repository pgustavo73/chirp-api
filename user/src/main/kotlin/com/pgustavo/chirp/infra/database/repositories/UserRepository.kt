package com.pgustavo.chirp.infra.database.repositories

import com.pgustavo.chirp.domain.model.UserID
import com.pgustavo.chirp.infra.database.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<UserEntity, UserID> {
    fun  findByEmail(email: String): UserEntity?

    fun findByEmailOrUsername(email: String, username: String): UserEntity?

}