package com.burton.chat.core.data.mapper

import com.burton.chat.core.data.local.entity.ContactEntity
import com.burton.chat.core.data.local.entity.ConversationEntity
import com.burton.chat.core.data.local.entity.MessageEntity
import com.burton.chat.core.model.Contact
import com.burton.chat.core.model.ConversationDetails
import com.burton.chat.core.model.ConversationPreview
import com.burton.chat.core.model.ConversationType
import com.burton.chat.core.model.Message
import com.burton.chat.core.model.MessageStatus

fun ContactEntity.toModel(): Contact = Contact(
    id = id,
    displayName = displayName,
    phoneNumber = phoneNumber,
    about = about,
    isMe = isCurrentUser,
    avatarSeed = avatarSeed,
)

fun ConversationEntity.toType(): ConversationType =
    runCatching { ConversationType.valueOf(type) }.getOrDefault(ConversationType.DIRECT)

fun MessageEntity.toModel(sender: Contact): Message = Message(
    id = id,
    conversationId = conversationId,
    sender = sender,
    body = body,
    createdAt = createdAt,
    status = runCatching { MessageStatus.valueOf(status) }.getOrDefault(MessageStatus.SENT),
)

fun ConversationEntity.toDetails(members: List<Contact>, isMember: Boolean): ConversationDetails {
    val type = toType()
    val title = when (type) {
        ConversationType.GROUP -> title.orEmpty().ifBlank { "Group" }
        ConversationType.DIRECT -> members.firstOrNull { !it.isMe }?.displayName ?: "Chat"
    }
    return ConversationDetails(
        id = id,
        type = type,
        title = title,
        members = members,
        createdAt = createdAt,
        isMember = isMember,
    )
}

fun ConversationEntity.toPreview(
    members: List<Contact>,
    lastMessage: MessageEntity?,
    unreadCount: Int,
): ConversationPreview {
    val type = toType()
    val peer = members.firstOrNull { !it.isMe }
    val title = when (type) {
        ConversationType.GROUP -> title.orEmpty().ifBlank { "Group" }
        ConversationType.DIRECT -> peer?.displayName ?: "Chat"
    }
    val subtitle = lastMessage?.let { message ->
        val senderName = members.firstOrNull { it.id == message.senderId }?.displayName
        if (type == ConversationType.GROUP && senderName != null) {
            "$senderName: ${message.body}"
        } else {
            message.body
        }
    } ?: "No messages yet"
    return ConversationPreview(
        id = id,
        type = type,
        title = title,
        subtitle = subtitle,
        lastMessageAt = lastMessageAt,
        unreadCount = unreadCount,
        avatarSeed = if (type == ConversationType.GROUP) {
            title.hashCode()
        } else {
            peer?.avatarSeed ?: 0
        },
        isGroup = type == ConversationType.GROUP,
    )
}
