package com.pgustavo.chirp.infra.database.repositories

import com.pgustavo.chirp.domain.type.UserId
import com.pgustavo.chirp.domain.models.ChatId
import com.pgustavo.chirp.infra.database.entities.ChatEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ChatRepository: JpaRepository<ChatEntity, ChatId> {
    @Query("""
         SELECT c 
         FROM ChatEntity c
         LEFT  JOIN FETCH c.participants
         LEFT JOIN FETCH c.creator
         WHERE c.id = :id
         AND EXISTS (
         SELECT 1
         FROM c.participants p
         WHERE p.userId = :userId
         )
    """)
    fun findChatById(id: ChatId, userId: UserId?): ChatEntity?
    // Query 1: Query all chats by user
    // Query 2: Query all the participants of chaT A
    // Query 3: Query the creator of that chaT A
    // Query 4: Query all the participants of chaT b
    // Query 5: Query the creator of that chaT B
    // Query 6: Query all the participants of chaT C
    // Query 7: Query the creator of that chaT C
    @Query("""
        FROM ChatEntity c
         LEFT  JOIN FETCH c.participants
         LEFT JOIN FETCH c.creator
         WHERE EXISTS (
         SELECT 1
         FROM c.participants p
         WHERE p.userId = :userId
         )
    """)
    fun findAllByUserId(userId: UserId): List<ChatEntity>
}