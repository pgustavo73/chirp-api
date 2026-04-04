package com.pgustavo.chirp.domain.infra.message_queue

import com.pgustavo.chirp.domain.events.ChirpEvent
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component
import java.lang.Exception

@Component
class EventPublisher(
    private val rabbitTemplate: RabbitTemplate
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun <T: ChirpEvent> publish(event: T){
        try {
            rabbitTemplate.convertAndSend(
                event.exchange,
                event.eventKey,
                event
            )
        } catch (e: Exception) {
            logger.error("Failed to publish ${event.eventKey} event", e)
        }
    }
}