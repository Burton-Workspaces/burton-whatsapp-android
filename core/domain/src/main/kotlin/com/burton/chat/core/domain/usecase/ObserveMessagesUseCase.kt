package com.burton.chat.core.domain.usecase

import com.burton.chat.core.domain.repository.MessageRepository
import com.burton.chat.core.model.Message
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveMessagesUseCase @Inject constructor(
    private val messageRepository: MessageRepository,
) {
    operator fun invoke(conversationId: String): Flow<List<Message>> =
        messageRepository.observeMessages(conversationId)
}
