package com.burton.chat.core.model

data class Contact(
    val id: String,
    val displayName: String,
    val phoneNumber: String,
    val about: String,
    val isMe: Boolean,
    val avatarSeed: Int,
) {
    val initials: String
        get() = displayName
            .split(" ")
            .filter { it.isNotBlank() }
            .take(2)
            .joinToString("") { it.first().uppercase() }
            .ifBlank { "?" }
}
