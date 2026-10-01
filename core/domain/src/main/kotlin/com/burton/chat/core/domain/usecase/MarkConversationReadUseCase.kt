package com.burton.chat.core.domain.usecase

import com.burton.chat.core.domain.repository.ConversationRepository
import javax.inject.Inject

class MarkConversationReadUseCase @Inject constructor(
    private val conversationRepository: ConversationRepository,
) {
    suspend operator fun invoke(conversationId: String) {
        conversationRepository.markRead(conversationId)
    }
}
