package com.burton.chat.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "conversations")
data class ConversationEntity(
    @PrimaryKey val id: String,
    val title: String?,
    val type: String,
    val createdAt: Long,
    val lastMessageAt: Long,
)
