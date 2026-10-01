package com.burton.chat.core.data.repository

import com.burton.chat.core.common.CurrentUser
import com.burton.chat.core.data.local.dao.ContactDao
import com.burton.chat.core.data.local.dao.ConversationDao
import com.burton.chat.core.data.local.dao.ConversationMemberDao
import com.burton.chat.core.data.local.dao.MessageDao
import com.burton.chat.core.data.local.dao.ReadStateDao
import com.burton.chat.core.data.local.entity.ConversationEntity
import com.burton.chat.core.data.local.entity.ConversationMemberEntity
import com.burton.chat.core.data.local.entity.MessageEntity
import com.burton.chat.core.data.local.entity.ReadStateEntity
import com.burton.chat.core.data.mapper.toDetails
import com.burton.chat.core.data.mapper.toModel
import com.burton.chat.core.data.mapper.toPreview
import com.burton.chat.core.domain.repository.ConversationRepository
import com.burton.chat.core.model.ConversationDetails
import com.burton.chat.core.model.ConversationPreview
import com.burton.chat.core.model.ConversationType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConversationRepositoryImpl @Inject constructor(
    private val conversationDao: ConversationDao,
    private val memberDao: ConversationMemberDao,
    private val contactDao: ContactDao,
    private val messageDao: MessageDao,
    private val readStateDao: ReadStateDao,
) : ConversationRepository {

    override fun observeChats(query: String): Flow<List<ConversationPreview>> =
        observePreviews { preview, isMember ->
            isMember && matches(preview, query)
        }

    override fun observeMyGroups(query: String): Flow<List<ConversationPreview>> =
        observePreviews { preview, isMember ->
            isMember && preview.isGroup && matches(preview, query)
        }

    override fun observeDiscoverableGroups(query: String): Flow<List<ConversationPreview>> =
        observePreviews { preview, isMember ->
            !isMember && preview.isGroup && matches(preview, query)
        }

    override fun observeConversation(id: String): Flow<ConversationDetails?> =
        combine(
            conversationDao.observeById(id),
            memberDao.observeForConversation(id),
            contactDao.observeAll(),
        ) { conversation, members, contacts ->
            conversation ?: return@combine null
            val contactMap = contacts.associateBy { it.id }
            val people = members.mapNotNull { contactMap[it.contactId]?.toModel() }
            conversation.toDetails(
                members = people,
                isMember = members.any { it.contactId == CurrentUser.ID },
            )
        }

    override suspend fun getOrCreateDirectConversation(contactId: String): String {
        conversationDao.findDirectBetween(CurrentUser.ID, contactId)?.let { return it.id }
        val now = System.currentTimeMillis()
        val id = "dm-${UUID.randomUUID()}"
        conversationDao.upsert(
            ConversationEntity(
                id = id,
                title = null,
                type = ConversationType.DIRECT.name,
                createdAt = now,
                lastMessageAt = now,
            ),
        )
        memberDao.upsertAll(
            listOf(
                ConversationMemberEntity(conversationId = id, contactId = CurrentUser.ID, role = "MEMBER"),
                ConversationMemberEntity(conversationId = id, contactId = contactId, role = "MEMBER"),
            ),
        )
        return id
    }

    override suspend fun createGroup(name: String, memberIds: Collection<String>): String {
        val now = System.currentTimeMillis()
        val id = "group-${UUID.randomUUID()}"
        conversationDao.upsert(
            ConversationEntity(
                id = id,
                title = name,
                type = ConversationType.GROUP.name,
                createdAt = now,
                lastMessageAt = now,
            ),
        )
        val members = (memberIds + CurrentUser.ID).distinct().map { contactId ->
            ConversationMemberEntity(
                conversationId = id,
                contactId = contactId,
                role = if (contactId == CurrentUser.ID) "ADMIN" else "MEMBER",
            )
        }
        memberDao.upsertAll(members)
        messageDao.upsert(
            MessageEntity(
                id = UUID.randomUUID().toString(),
                conversationId = id,
                senderId = CurrentUser.ID,
                body = "You created the group $name",
                createdAt = now,
                status = "SENT",
            ),
        )
        return id
    }

    override suspend fun joinGroup(conversationId: String) {
        if (memberDao.isMember(conversationId, CurrentUser.ID)) return
        memberDao.upsertAll(
            listOf(
                ConversationMemberEntity(
                    conversationId = conversationId,
                    contactId = CurrentUser.ID,
                    role = "MEMBER",
                ),
            ),
        )
        val now = System.currentTimeMillis()
        messageDao.upsert(
            MessageEntity(
                id = UUID.randomUUID().toString(),
                conversationId = conversationId,
                senderId = CurrentUser.ID,
                body = "You joined the group",
                createdAt = now,
                status = "SENT",
            ),
        )
        conversationDao.updateLastMessageAt(conversationId, now)
    }

    override suspend fun leaveGroup(conversationId: String) {
        memberDao.remove(conversationId, CurrentUser.ID)
    }

    override suspend fun markRead(conversationId: String) {
        readStateDao.upsert(ReadStateEntity(conversationId, System.currentTimeMillis()))
    }

    private fun observePreviews(
        predicate: (ConversationPreview, Boolean) -> Boolean,
    ): Flow<List<ConversationPreview>> = combine(
        conversationDao.observeAll(),
        memberDao.observeAll(),
        contactDao.observeAll(),
        messageDao.observeAll(),
        readStateDao.observeAll(),
    ) { conversations, members, contacts, messages, readState ->
        val contactMap = contacts.associateBy { it.id }
        val membersByConversation = members.groupBy { it.conversationId }
        val lastMessages = messages.groupBy { it.conversationId }
            .mapValues { (_, items) -> items.maxByOrNull { it.createdAt } }
        val readByConversation = readState.associateBy { it.conversationId }
        conversations.map { conversation ->
            val conversationMembers = membersByConversation[conversation.id].orEmpty()
            val people = conversationMembers.mapNotNull { contactMap[it.contactId]?.toModel() }
            val lastMessage = lastMessages[conversation.id]
            val lastReadAt = readByConversation[conversation.id]?.lastReadAt ?: 0L
            val unread = messages.count { message ->
                message.conversationId == conversation.id &&
                    message.senderId != CurrentUser.ID &&
                    message.createdAt > lastReadAt
            }
            val isMember = conversationMembers.any { it.contactId == CurrentUser.ID }
            conversation.toPreview(people, lastMessage, unread) to isMember
        }.filter { (preview, isMember) -> predicate(preview, isMember) }
            .map { it.first }
    }

    private fun matches(preview: ConversationPreview, query: String): Boolean {
        if (query.isBlank()) return true
        val needle = query.trim()
        return preview.title.contains(needle, ignoreCase = true) ||
            preview.subtitle.contains(needle, ignoreCase = true)
    }
}
