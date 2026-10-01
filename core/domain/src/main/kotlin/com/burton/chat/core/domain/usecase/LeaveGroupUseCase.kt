package com.burton.chat.core.domain.usecase

import com.burton.chat.core.domain.repository.ConversationRepository
import javax.inject.Inject

class LeaveGroupUseCase @Inject constructor(
    private val conversationRepository: ConversationRepository,
) {
    suspend operator fun invoke(conversationId: String) {
        conversationRepository.leaveGroup(conversationId)
    }
}
