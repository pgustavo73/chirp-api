package com.pgustavo.chirp.infra.messaging

import com.pgustavo.chirp.domain.events.user.UserEvent
import com.pgustavo.chirp.domain.infra.message_queue.MessageQueues
import com.pgustavo.chirp.domain.models.ChatParticipant
import com.pgustavo.chirp.service.ChatParticipantService
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class ChatUserEventListener(
    private val chatParticipantService: ChatParticipantService,
) {

    @RabbitListener(queues = [MessageQueues.CHAT_USER_EVENTS])
    fun handleUserEvent(event: UserEvent) {
        when (event) {
            is UserEvent.Verified -> {
                chatParticipantService.createChatParticipant(
                    chatParticipant = ChatParticipant(
                        userId = event.userId,
                        username = event.username,
                        email = event.email,
                        profilePictureUrl = null,
                    )
                )
            }
            else -> Unit
        }
    }
}