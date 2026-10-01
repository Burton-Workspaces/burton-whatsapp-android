package com.burton.chat.core.domain.usecase

import com.burton.chat.core.domain.repository.ConversationRepository
import javax.inject.Inject

class JoinGroupUseCase @Inject constructor(
    private val conversationRepository: ConversationRepository,
) {
    suspend operator fun invoke(conversationId: String) {
        conversationRepository.joinGroup(conversationId)
    }
}
