package com.burton.chat.core.domain.repository

import com.burton.chat.core.model.ConversationDetails
import com.burton.chat.core.model.ConversationPreview
import kotlinx.coroutines.flow.Flow

interface ConversationRepository {
    fun observeChats(query: String = ""): Flow<List<ConversationPreview>>
    fun observeMyGroups(query: String = ""): Flow<List<ConversationPreview>>
    fun observeDiscoverableGroups(query: String = ""): Flow<List<ConversationPreview>>
    fun observeConversation(id: String): Flow<ConversationDetails?>
    suspend fun getOrCreateDirectConversation(contactId: String): String
    suspend fun createGroup(name: String, memberIds: Collection<String>): String
    suspend fun joinGroup(conversationId: String)
    suspend fun leaveGroup(conversationId: String)
    suspend fun markRead(conversationId: String)
}
