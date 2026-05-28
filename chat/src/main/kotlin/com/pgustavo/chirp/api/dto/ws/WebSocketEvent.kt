package com.pgustavo.chirp.api.dto.ws

enum class IncomingWebSocketMessageType {
    NEW_MESSAGE,
}

enum class OutgoingWebSocketMessageType {
    NEW_MESSAGE,
    MESSAGE_DELETED,
    PROFILE_PICTURE_UPDATED,
    CHAT_PARTICIPANT_CHANGE,
    ERROR,
}

data class IncomingWebSocketMessage(
    val type: IncomingWebSocketMessageType,
    val payload: String,
)

data class OutgoingWebSocketMessage(
    val type: OutgoingWebSocketMessageType,
    val payload: String,
)