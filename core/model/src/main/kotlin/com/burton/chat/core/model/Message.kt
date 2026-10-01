package com.burton.chat.core.model

data class Message(
    val id: String,
    val conversationId: String,
    val sender: Contact,
    val body: String,
    val createdAt: Long,
    val status: MessageStatus,
) {
    val isMine: Boolean get() = sender.isMe
}
