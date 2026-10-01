package com.burton.chat.core.data.repository

import com.burton.chat.core.common.CurrentUser
import com.burton.chat.core.data.local.dao.ContactDao
import com.burton.chat.core.data.local.dao.ConversationDao
import com.burton.chat.core.data.local.dao.MessageDao
import com.burton.chat.core.data.local.entity.MessageEntity
import com.burton.chat.core.data.mapper.toModel
import com.burton.chat.core.data.simulator.IncomingMessageSimulator
import com.burton.chat.core.domain.repository.MessageRepository
import com.burton.chat.core.model.Contact
import com.burton.chat.core.model.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageRepositoryImpl @Inject constructor(
    private val messageDao: MessageDao,
    private val conversationDao: ConversationDao,
    private val contactDao: ContactDao,
    private val simulator: IncomingMessageSimulator,
) : MessageRepository {
    override fun observeMessages(conversationId: String): Flow<List<Message>> =
        combine(
            messageDao.observeForConversation(conversationId),
            contactDao.observeAll(),
        ) { messages, contacts ->
            val contactMap = contacts.associate { it.id to it.toModel() }
            val unknown = Contact(
                id = "unknown",
                displayName = "Unknown",
                phoneNumber = "",
                about = "",
                isMe = false,
                avatarSeed = 0,
            )
            messages.map { message ->
                message.toModel(contactMap[message.senderId] ?: unknown)
            }
        }

    override suspend fun sendMessage(conversationId: String, body: String) {
        val now = System.currentTimeMillis()
        messageDao.upsert(
            MessageEntity(
                id = UUID.randomUUID().toString(),
                conversationId = conversationId,
                senderId = CurrentUser.ID,
                body = body,
                createdAt = now,
                status = "SENT",
            ),
        )
        conversationDao.updateLastMessageAt(conversationId, now)
        simulator.onOutgoingMessage(conversationId)
    }
}
