package com.burton.chat.core.model

data class ConversationPreview(
    val id: String,
    val type: ConversationType,
    val title: String,
    val subtitle: String,
    val lastMessageAt: Long,
    val unreadCount: Int,
    val avatarSeed: Int,
    val isGroup: Boolean,
)
