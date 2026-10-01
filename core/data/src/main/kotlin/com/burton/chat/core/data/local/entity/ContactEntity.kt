package com.burton.chat.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class ContactEntity(
    @PrimaryKey val id: String,
    val displayName: String,
    val phoneNumber: String,
    val about: String,
    val isCurrentUser: Boolean,
    val avatarSeed: Int,
)
