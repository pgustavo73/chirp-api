package com.pgustavo.chirp.infra.message_queue

import com.pgustavo.chirp.domain.events.user.UserEvent
import com.pgustavo.chirp.domain.infra.message_queue.MessageQueues
import com.pgustavo.chirp.service.EmailService
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Duration

@Component
class NotificationUserEventListener(private val emailService: EmailService) {

    @RabbitListener(queues = [MessageQueues.NOTIFICATION_USER_EVENTS])
    @Transactional
    fun handleUserEvent(event: UserEvent) {
        when (event) {
            is UserEvent.Created -> {
                emailService.sendVerificationEmail(event.email, event.username, event.userId, event.verificationToken)
                println("User Created!")
            }

            is UserEvent.RequestResendVerification -> {
                emailService.sendVerificationEmail(event.email, event.username, event.userId, event.verificationToken)
                println("Request resend verification!")
            }

            is UserEvent.RequestResetPassword -> {
                emailService.sendPasswordResetEmail(
                    event.email, event.username, event.userId, event.passwordResetToken,
                    Duration.ofMinutes(event.expiresInMinutes)
                )
                println("Request resend Password!")
            }

            else -> Unit
        }
    }
}