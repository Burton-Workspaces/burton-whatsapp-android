package com.burton.chat.core.domain.usecase

import com.burton.chat.core.domain.repository.ConversationRepository
import com.burton.chat.core.model.ConversationPreview
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveMyGroupsUseCase @Inject constructor(
    private val conversationRepository: ConversationRepository,
) {
    operator fun invoke(query: String = ""): Flow<List<ConversationPreview>> =
        conversationRepository.observeMyGroups(query)
}
