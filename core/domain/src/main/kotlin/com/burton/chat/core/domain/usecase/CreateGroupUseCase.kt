package com.burton.chat.core.domain.usecase

import com.burton.chat.core.domain.repository.ConversationRepository
import javax.inject.Inject

class CreateGroupUseCase @Inject constructor(
    private val conversationRepository: ConversationRepository,
) {
    suspend operator fun invoke(name: String, memberIds: Collection<String>): String {
        val trimmed = name.trim()
        require(trimmed.isNotEmpty()) { "Group name cannot be blank." }
        require(memberIds.isNotEmpty()) { "Select at least one member." }
        return conversationRepository.createGroup(trimmed, memberIds)
    }
}
