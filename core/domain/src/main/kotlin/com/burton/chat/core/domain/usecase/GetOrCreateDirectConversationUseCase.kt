package com.burton.chat.core.domain.usecase

import com.burton.chat.core.domain.repository.ConversationRepository
import javax.inject.Inject

class GetOrCreateDirectConversationUseCase @Inject constructor(
    private val conversationRepository: ConversationRepository,
) {
    suspend operator fun invoke(contactId: String): String =
        conversationRepository.getOrCreateDirectConversation(contactId)
}
