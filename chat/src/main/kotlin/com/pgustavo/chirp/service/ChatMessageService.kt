package com.pgustavo.chirp.service

import com.pgustavo.chirp.domain.exception.ChatNotFoundException
import com.pgustavo.chirp.domain.exception.MessageNotFoundException
import com.pgustavo.chirp.domain.execption.ForbiddenException
import com.pgustavo.chirp.domain.models.ChatId
import com.pgustavo.chirp.domain.models.ChatMessage
import com.pgustavo.chirp.domain.models.ChatMessageId
import com.pgustavo.chirp.domain.type.UserId
import com.pgustavo.chirp.infra.database.entities.ChatMessageEntity
import com.pgustavo.chirp.infra.database.mappers.toChatMessage
import com.pgustavo.chirp.infra.database.repositories.ChatMessageRepository
import com.pgustavo.chirp.infra.database.repositories.ChatParticipantRepository
import com.pgustavo.chirp.infra.database.repositories.ChatRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ChatMessageService(
    private val chatRepository: ChatRepository,
    private val chatMessageRepository: ChatMessageRepository,
    private val chatParticipantRepository: ChatParticipantRepository,
) {

    @Transactional
    fun sendMessage(
        chatId: ChatId,
        senderId: UserId,
        content: String,
        messageId: ChatMessageId? = null
    ): ChatMessage {
        val chat = chatRepository.findChatById(chatId, senderId)
            ?: throw ChatNotFoundException()
        val sender = chatParticipantRepository.findByIdOrNull(senderId)
            ?: throw ChatNotFoundException()

        val savedMessage = chatMessageRepository.save(
            ChatMessageEntity(
                id = messageId,
                content = content.trim(),
                chatId = chatId,
                chat = chat,
                sender = sender
            )
        )
        return savedMessage.toChatMessage()
    }

    @Transactional
    fun deleteMessage(
        messageId: ChatMessageId,
        requestUserId: UserId,
    ) {
        val message = chatMessageRepository.findByIdOrNull(messageId)
            ?: throw MessageNotFoundException(messageId)

        if (message.sender.userId != requestUserId) {
            throw ForbiddenException()
        }
        chatMessageRepository.delete(message)
    }
}