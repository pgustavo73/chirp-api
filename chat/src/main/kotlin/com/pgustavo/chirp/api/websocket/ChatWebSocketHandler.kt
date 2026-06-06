package com.pgustavo.chirp.api.websocket

import com.pgustavo.chirp.api.dto.ws.ChatParticipantsChangedDto
import com.pgustavo.chirp.api.dto.ws.DeleteMessageDto
import com.pgustavo.chirp.api.dto.ws.ErrorDto
import com.pgustavo.chirp.api.dto.ws.IncomingWebSocketMessage
import com.pgustavo.chirp.api.dto.ws.IncomingWebSocketMessageType
import com.pgustavo.chirp.api.dto.ws.OutgoingWebSocketMessage
import com.pgustavo.chirp.api.dto.ws.OutgoingWebSocketMessageType
import com.pgustavo.chirp.api.dto.ws.SendMessageDto
import com.pgustavo.chirp.api.mappers.toChatMessageDto
import com.pgustavo.chirp.domain.event.ChatParticipantJoinedEvent
import com.pgustavo.chirp.domain.event.ChatParticipantLeftEvent
import com.pgustavo.chirp.domain.event.MessageDeletedEvent
import com.pgustavo.chirp.domain.type.ChatId
import com.pgustavo.chirp.domain.type.UserId
import com.pgustavo.chirp.service.ChatMessageService
import com.pgustavo.chirp.service.ChatService
import com.pgustavo.chirp.service.JwtService
import com.rabbitmq.tools.jsonrpc.JsonRpcMappingException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.PingMessage
import org.springframework.web.socket.PongMessage
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import tools.jackson.databind.ObjectMapper
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

@Component
class ChatWebSocketHandler(
    private val chatMessageService: ChatMessageService,
    private val objectMapper: ObjectMapper,
    private val chatService: ChatService,
    private val jwtService: JwtService
) : TextWebSocketHandler() {

    companion object {
        private const val PING_INTERVAL_MS = 30_000L
        private const val PONG_TIMEOUT_MS = 60_000L
    }

    private val logger = LoggerFactory.getLogger(javaClass)

    private val connectionLock = ReentrantReadWriteLock()

    private val sessions = ConcurrentHashMap<String, UserSession>()
    private val userToSessions = ConcurrentHashMap<UserId, MutableSet<String>>()
    private val userChatIds = ConcurrentHashMap<UserId, MutableSet<ChatId>>()
    private val chatToSessions = ConcurrentHashMap<ChatId, MutableSet<String>>()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val authHeader = session
            .handshakeHeaders
            .getFirst(HttpHeaders.AUTHORIZATION)
            ?: run {
                logger.warn("Session ${session.id} was closed due to missing Authorization header")
                session.close(CloseStatus.SERVER_ERROR.withReason("Authentication failed"))
                return
            }

        val userId = jwtService.getUserIdFromToken(authHeader)

        val userSession = UserSession(
            userId = userId,
            session = session
        )

        connectionLock.write {
            sessions[session.id] = userSession

            userToSessions.compute(userId) { _, existingSessions ->
                (existingSessions ?: mutableSetOf()).apply {
                    add(session.id)
                }
            }

            val chatIds = userChatIds.computeIfAbsent(userId) {
                val chatIds = chatService.findChatsByUser(userId).map { it.id }
                ConcurrentHashMap.newKeySet<ChatId>().apply {
                    addAll(chatIds)
                }
            }

            chatIds.forEach { chatId ->
                chatToSessions.compute(chatId) { _, sessions ->
                    (sessions ?: mutableSetOf()).apply {
                        add(session.id)
                    }
                }
            }
        }

        logger.info("Websocket connection established for user $userId")
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        logger.debug("Received message ${message.payload}")

        val userSession = connectionLock.read {
            sessions[session.id] ?: return
        }

        try {
            val webSocketMessage = objectMapper.readValue(
                message.payload,
                IncomingWebSocketMessage::class.java
            )
            when(webSocketMessage.type) {
               IncomingWebSocketMessageType.NEW_MESSAGE -> {
                   val dto = objectMapper.readValue(
                       webSocketMessage.payload,
                       SendMessageDto::class.java
                   )
                   handSendMessage(
                       dto,
                       userSession.userId
                   )
               }
            }
        } catch (e: JsonRpcMappingException){
            logger.error("Could not parse message ${message.payload}")
            sendError(
                userSession.session,
                ErrorDto(
                    "INVALID_JSON",
                    "Incoming JSON or UUID is invalid"
                )
            )
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onDeleteMessage(event: MessageDeletedEvent){
        broadcastToChat(
            chatId = event.chatId,
            message = OutgoingWebSocketMessage(
                type = OutgoingWebSocketMessageType.MESSAGE_DELETED,
                payload = objectMapper.writeValueAsString(
                    DeleteMessageDto(
                        chatId = event.chatId,
                        messageId = event.messageId,
                    )
                )
            )
        )
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onJoinChat(event: ChatParticipantJoinedEvent){
        connectionLock.write {
            event.userIds.forEach { userId ->
                userChatIds.compute(userId) { _, chatIds ->
                    (chatIds ?: mutableSetOf()).apply {
                        add(event.chatId)
                    }
                }

                userToSessions[userId]?.forEach { sessionId ->
                    chatToSessions.compute(event.chatId) { _, sessions ->
                        (sessions ?: mutableSetOf()).apply { add(sessionId)}
                    }
                }
            }
        }

        broadcastToChat(
            chatId = event.chatId,
            message = OutgoingWebSocketMessage(
                type = OutgoingWebSocketMessageType.CHAT_PARTICIPANT_CHANGE,
                payload = objectMapper.writeValueAsString(
                    ChatParticipantsChangedDto(
                        chatId = event.chatId
                    )
                )
            )
        )
    }

    override fun handlePongMessage(session: WebSocketSession, message: PongMessage) {
        connectionLock.write{
            sessions.compute(session.id) { _, userSession ->
                userSession?.copy(
                    lastPongTimeStamp = System.currentTimeMillis(),
                )
            }
        }
        logger.debug("Received pong from ${session.id}")
    }

    @Scheduled(fixedDelay = PING_INTERVAL_MS)
    fun pingClients() {
        val currentTime = System.currentTimeMillis()
        val sessionsToClose = mutableListOf<String>()

        val sessionsSnapShot = connectionLock.read {
            sessions.toMap()
        }

        sessionsSnapShot.forEach { sessionId, userSession ->
            try {
                val lastPong = userSession.lastPongTimeStamp
                if (currentTime - lastPong > PING_INTERVAL_MS) {
                    logger.warn("Session $sessionId has timed out, closing connection")
                    sessionsToClose.add(sessionId)
                }
                userSession.session.sendMessage(PingMessage())
                logger.debug("Sent ping to {}", userSession.userId)
            } catch (e: Exception){
                logger.error("Could not ping session $sessionId", e)
                sessionsToClose.add(sessionId)
            }
        }

        sessionsToClose.forEach { sessionId ->
            connectionLock.read {
                sessions[sessionId]?.session?.let { session ->
                    try {
                        session.close(CloseStatus.GOING_AWAY.withReason("Ping timeout"))
                    }catch (e: Exception){
                        logger.error("Couldn't close sessions for session $sessionId", e)
                    }
                }
            }
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onLeftChat(event: ChatParticipantLeftEvent){
        connectionLock.write {
            userChatIds.compute( event.userId) { _, chatIds ->
                chatIds
                    ?.apply { remove(event.chatId) }
                    ?.takeIf { it.isNotEmpty() }
            }

            userToSessions[event.userId]?.forEach { sessionId ->
                chatToSessions.compute( event.chatId) { _, sessions ->
                    sessions
                        ?.apply { remove(sessionId) }
                        ?.takeIf { it.isNotEmpty() }
                }
            }
        }

        broadcastToChat(
            chatId = event.chatId,
            message = OutgoingWebSocketMessage(
                type = OutgoingWebSocketMessageType.CHAT_PARTICIPANT_CHANGE,
                payload = objectMapper.writeValueAsString(
                    ChatParticipantsChangedDto(
                        chatId = event.chatId
                    )
                )
            )
        )
    }

    private fun sendError(
        session: WebSocketSession,
        error: ErrorDto,
    ) {
        val webSocketMessage = objectMapper.writeValueAsString(
            OutgoingWebSocketMessage(
                OutgoingWebSocketMessageType.ERROR,
                objectMapper.writeValueAsString(error),
            )
        )

        try {
            session.sendMessage(TextMessage(webSocketMessage))
        }catch (e: Exception){
            logger.error("Couldn't send message", e)
        }
    }

    private fun broadcastToChat(
        chatId: ChatId,
        message: OutgoingWebSocketMessage
    ) {
        val chatSessions = connectionLock.read {
            chatToSessions[chatId]?.toList() ?: emptyList()
        }

        chatSessions.forEach {
            val userSession = connectionLock.read {
                sessions[it]
            } ?: return@forEach

            sendToUser(
                userSession.userId,
                message,
            )
        }
    }

    private fun handSendMessage(
        dto: SendMessageDto,
        senderId: UserId,
    ) {
        val userChatsIds = connectionLock.read { userChatIds[senderId] } ?: return

        if (dto.chatId !in userChatsIds) {
            return
        }

        val savedMessage = chatMessageService.sendMessage(
            dto.chatId,
            senderId,
            dto.content,
            dto.messageId,
        )

        broadcastToChat(
            dto.chatId,
            OutgoingWebSocketMessage(
                type = OutgoingWebSocketMessageType.NEW_MESSAGE,
                payload = objectMapper.writeValueAsString(savedMessage.toChatMessageDto()),
            )
        )
    }

    private fun sendToUser(userId: UserId, message: OutgoingWebSocketMessage) {
        val userSessions = connectionLock.read {
            userToSessions[userId] ?: emptySet()
        }
        userSessions.forEach { sessionId ->
            val userSession = connectionLock.read {
                sessions[sessionId] ?: return@forEach
            }
            if (userSession.session.isOpen) {
                try {
                    val messageJson = objectMapper.writeValueAsString(message)
                    userSession.session.sendMessage(TextMessage(messageJson))
                    logger.debug("Sent message to user {}: {}", userId, messageJson)
                } catch (e: Exception) {
                    logger.error("Error while sending message to user $userId", e)
                }
            }
        }
    }

    private data class UserSession(
        val userId: UserId,
        val session: WebSocketSession,
        val lastPongTimeStamp: Long = System.currentTimeMillis()
    )
}