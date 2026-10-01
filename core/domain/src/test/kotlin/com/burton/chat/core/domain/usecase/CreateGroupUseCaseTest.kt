package com.burton.chat.core.domain.usecase

import com.burton.chat.core.domain.repository.ConversationRepository
import com.burton.chat.core.model.ConversationDetails
import com.burton.chat.core.model.ConversationPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CreateGroupUseCaseTest {
    @Test
    fun `requires a group name and members`() = runTest {
        val repository = FakeConversationRepository()
        val useCase = CreateGroupUseCase(repository)
        assertTrue(runCatching { useCase("  ", listOf("c1")) }.isFailure)
        assertTrue(runCatching { useCase("Weekend", emptyList()) }.isFailure)
        assertTrue(repository.created.isEmpty())
    }

    @Test
    fun `creates a group with trimmed name`() = runTest {
        val repository = FakeConversationRepository()
        val useCase = CreateGroupUseCase(repository)
        val id = useCase("  Burton Team  ", listOf("maya", "jordan"))
        assertEquals("group-1", id)
        assertEquals("Burton Team" to setOf("maya", "jordan"), repository.created.single())
    }

    private class FakeConversationRepository : ConversationRepository {
        val created = mutableListOf<Pair<String, Set<String>>>()

        override fun observeChats(query: String): Flow<List<ConversationPreview>> =
            MutableStateFlow(emptyList())

        override fun observeMyGroups(query: String): Flow<List<ConversationPreview>> =
            MutableStateFlow(emptyList())

        override fun observeDiscoverableGroups(query: String): Flow<List<ConversationPreview>> =
            MutableStateFlow(emptyList())

        override fun observeConversation(id: String): Flow<ConversationDetails?> =
            MutableStateFlow(null)

        override suspend fun getOrCreateDirectConversation(contactId: String): String = "dm"

        override suspend fun createGroup(name: String, memberIds: Collection<String>): String {
            created += name to memberIds.toSet()
            return "group-1"
        }

        override suspend fun joinGroup(conversationId: String) = Unit

        override suspend fun leaveGroup(conversationId: String) = Unit

        override suspend fun markRead(conversationId: String) = Unit
    }
}
