package com.pgustavo.chirp.infra.message_queue

import com.pgustavo.chirp.domain.events.user.UserEvent
import com.pgustavo.chirp.domain.infra.message_queue.MessageQueues
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class NotificationUserEventListener {

    @RabbitListener(queues = [MessageQueues.NOTIFICATION_USER_EVENTS])
    @Transactional
    fun handleUserEvent(event: UserEvent) {
        when(event){
            is UserEvent.Created -> { println("User Created!")}
            is UserEvent.RequestResendVerification -> {println("Request resend verification!")}
            is UserEvent.RequestResetPassword -> {println("Request resend Password!")}
            else -> Unit
        }
    }
}