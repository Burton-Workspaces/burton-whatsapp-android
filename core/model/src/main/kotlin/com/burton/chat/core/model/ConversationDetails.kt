package com.burton.chat.core.model

data class ConversationDetails(
    val id: String,
    val type: ConversationType,
    val title: String,
    val members: List<Contact>,
    val createdAt: Long,
    val isMember: Boolean,
) {
    val memberCount: Int get() = members.size

    val otherMembers: List<Contact> get() = members.filterNot { it.isMe }

    val directPeer: Contact? get() = otherMembers.firstOrNull()
}
