package com.pgustavo.chat.infra.database.repositories

import com.pgustavo.chat.domain.events.user.type.UserId
import com.pgustavo.chat.infra.database.entities.ChatParticipantEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ChatParticipantRepository: JpaRepository<ChatParticipantEntity, UserId> {
    fun findByUserIdIn(userIds: List<UserId>): Set<ChatParticipantEntity>
    @Query(""" SELECT p 
        FROM ChatParticipantEntity p
        WHERE LOWER(p.username) = :query OR LOWER(p.email) = :query
    """)
    fun findByEmailOrUsername(query: String): ChatParticipantEntity?
}