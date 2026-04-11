package com.pgustavo.chirp.api.controllers

import com.pgustavo.chirp.api.util.requestUserId
import com.pgustavo.chirp.domain.models.ChatMessageId
import com.pgustavo.chirp.service.ChatMessageService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/messages")
class ChatMessageController(private val chatMessageService: ChatMessageService) {

    @DeleteMapping("/{messageId}")
    fun deleteMessage(
        @PathVariable("messageId") messageId: ChatMessageId
    ){
        chatMessageService.deleteMessage(messageId, requestUserId)
    }
}