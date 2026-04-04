package com.pgustavo.chirp.service

import com.pgustavo.chirp.domain.models.ChatParticipant
import com.pgustavo.chirp.domain.type.UserId
import com.pgustavo.chirp.infra.database.mappers.toChatParticipant
import com.pgustavo.chirp.infra.database.mappers.toChatParticipantEntity
import com.pgustavo.chirp.infra.database.repositories.ChatParticipantRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.management.Query

@Service
class ChatParticipantService(
    private val chatParticipantRepository: ChatParticipantRepository
) {

    fun createChatParticipant(
        chatParticipant: ChatParticipant
    ){
        chatParticipantRepository.save(
            chatParticipant.toChatParticipantEntity()
        )
    }

    fun findChatParticipantById(userId: UserId): ChatParticipant? {
        return chatParticipantRepository.findByIdOrNull(userId)?.toChatParticipant()
    }

    fun findChatParticipantByEmailOrUsername(
        query: String
    ): ChatParticipant? {
        val normalizeQuery = query.lowercase().trim()
        return chatParticipantRepository.findByEmailOrUsername(
            query = normalizeQuery
        )?.toChatParticipant()
    }
}