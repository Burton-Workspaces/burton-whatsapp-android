package com.burton.chat.core.domain.usecase

import com.burton.chat.core.domain.repository.ConversationRepository
import com.burton.chat.core.model.ConversationDetails
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveConversationUseCase @Inject constructor(
    private val conversationRepository: ConversationRepository,
) {
    operator fun invoke(conversationId: String): Flow<ConversationDetails?> =
        conversationRepository.observeConversation(conversationId)
}
