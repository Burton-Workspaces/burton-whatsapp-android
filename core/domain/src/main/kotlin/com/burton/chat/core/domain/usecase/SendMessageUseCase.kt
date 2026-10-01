package com.burton.chat.core.domain.usecase

import com.burton.chat.core.domain.repository.MessageRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val messageRepository: MessageRepository,
) {
    suspend operator fun invoke(conversationId: String, body: String) {
        val trimmed = body.trim()
        require(trimmed.isNotEmpty()) { "Message cannot be blank." }
        messageRepository.sendMessage(conversationId, trimmed)
    }
}
