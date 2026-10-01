package com.burton.chat.core.data.simulator

import com.burton.chat.core.data.local.dao.ConversationDao
import com.burton.chat.core.data.local.dao.ConversationMemberDao
import com.burton.chat.core.data.local.dao.MessageDao
import com.burton.chat.core.data.local.entity.MessageEntity
import com.burton.chat.core.model.ConversationType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random
import com.burton.chat.core.common.CurrentUser
import com.burton.chat.core.data.di.ApplicationScope

@Singleton
class IncomingMessageSimulator @Inject constructor(
    private val messageDao: MessageDao,
    private val conversationDao: ConversationDao,
    private val memberDao: ConversationMemberDao,
    @param:ApplicationScope private val scope: CoroutineScope,
) {
    fun onOutgoingMessage(conversationId: String) {
        scope.launch {
            delay(Random.nextLong(700, 2_400))
            val conversation = conversationDao.getById(conversationId) ?: return@launch
            val others = memberDao.getForConversation(conversationId)
                .map { it.contactId }
                .filterNot { it == CurrentUser.ID }
            val senderId = others.randomOrNull() ?: return@launch
            if (conversation.type == ConversationType.GROUP.name && Random.nextFloat() > 0.45f) {
                return@launch
            }
            val now = System.currentTimeMillis()
            messageDao.upsert(
                MessageEntity(
                    id = UUID.randomUUID().toString(),
                    conversationId = conversationId,
                    senderId = senderId,
                    body = replies.random(),
                    createdAt = now,
                    status = "SENT",
                ),
            )
            conversationDao.updateLastMessageAt(conversationId, now)
        }
    }

    private val replies = listOf(
        "Got it.",
        "Sounds good.",
        "I can take that.",
        "On my way.",
        "Nice — thanks for the update.",
        "Can you send a bit more detail?",
        "Let's do it.",
        "I will check and reply in a few.",
        "That works for me.",
        "Noted.",
    )
}
