package com.pgustavo.chat.infra.database.repositories

import com.pgustavo.chat.domain.models.ChatId
import com.pgustavo.chat.domain.models.ChatMessageId
import com.pgustavo.chat.infra.database.entities.ChatMessageEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.Instant

interface ChatMessageRepository: JpaRepository<ChatMessageEntity, ChatMessageId> {

    @Query("""
        SELECT m
        FROM ChatMessageEntity m
        WHERE m.chatId = :chatId
        AND m.createAt < :before
        ORDER BY m.createdAt DESC
        """
    )
    fun findByChatIdBefore(
        chatId: ChatId,
        before: Instant,
        pageable: Pageable
    ): Slice<ChatMessageEntity>

    @Query("""
        SELECT m
        FROM ChatMessageEntity m
        LEFT JOIN FETCH m.sender
        WHERE m.chatId IN :chatsIds
        AND (m.createAt, m.id) = (
        SELECT m2.createAt, m2.id
        WHERE m2.chatId = m.chatId
        ORDER BY m2.createdAt DESC
        LIMIT 1
        )
        """)
    fun findLatestMessageByChatIds(
        chatIds: Set<ChatId>
    ): List<ChatMessageEntity>
}