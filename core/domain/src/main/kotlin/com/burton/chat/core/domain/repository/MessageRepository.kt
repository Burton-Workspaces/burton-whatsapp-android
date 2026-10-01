package com.burton.chat.core.domain.repository

import com.burton.chat.core.model.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    fun observeMessages(conversationId: String): Flow<List<Message>>
    suspend fun sendMessage(conversationId: String, body: String)
}
