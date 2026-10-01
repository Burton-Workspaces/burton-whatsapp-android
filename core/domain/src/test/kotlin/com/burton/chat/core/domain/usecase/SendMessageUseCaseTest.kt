package com.burton.chat.core.domain.usecase

import com.burton.chat.core.domain.repository.MessageRepository
import com.burton.chat.core.model.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SendMessageUseCaseTest {
    @Test
    fun `rejects blank messages`() = runTest {
        val repository = FakeMessageRepository()
        val useCase = SendMessageUseCase(repository)
        val result = runCatching { useCase("chat-1", "   ") }
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
        assertTrue(repository.sent.isEmpty())
    }

    @Test
    fun `trims and sends message body`() = runTest {
        val repository = FakeMessageRepository()
        val useCase = SendMessageUseCase(repository)
        useCase("chat-1", "  Hello Burton  ")
        assertEquals(listOf("chat-1" to "Hello Burton"), repository.sent)
    }

    private class FakeMessageRepository : MessageRepository {
        val sent = mutableListOf<Pair<String, String>>()

        override fun observeMessages(conversationId: String): Flow<List<Message>> =
            MutableStateFlow(emptyList())

        override suspend fun sendMessage(conversationId: String, body: String) {
            sent += conversationId to body
        }
    }
}
