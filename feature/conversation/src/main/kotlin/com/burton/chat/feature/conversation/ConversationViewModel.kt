package com.burton.chat.feature.conversation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.burton.chat.core.domain.usecase.MarkConversationReadUseCase
import com.burton.chat.core.domain.usecase.ObserveConversationUseCase
import com.burton.chat.core.domain.usecase.ObserveMessagesUseCase
import com.burton.chat.core.domain.usecase.SendMessageUseCase
import com.burton.chat.core.model.ConversationDetails
import com.burton.chat.core.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ConversationUiState(
    val details: ConversationDetails? = null,
    val messages: List<Message> = emptyList(),
    val draft: String = "",
    val sending: Boolean = false,
)

@HiltViewModel
class ConversationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    observeConversation: ObserveConversationUseCase,
    observeMessages: ObserveMessagesUseCase,
    private val sendMessage: SendMessageUseCase,
    private val markRead: MarkConversationReadUseCase,
) : ViewModel() {
    private val conversationId: String = checkNotNull(savedStateHandle["conversationId"])
    private val draft = MutableStateFlow("")

    val uiState: StateFlow<ConversationUiState> = combine(
        observeConversation(conversationId).filterNotNull(),
        observeMessages(conversationId),
        draft,
    ) { details, messages, currentDraft ->
        ConversationUiState(details = details, messages = messages, draft = currentDraft)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ConversationUiState())

    init {
        viewModelScope.launch {
            markRead(conversationId)
        }
    }

    fun onDraftChange(value: String) {
        draft.value = value
    }

    fun send() {
        val body = draft.value
        viewModelScope.launch {
            runCatching {
                sendMessage(conversationId, body)
                draft.value = ""
                markRead(conversationId)
            }
        }
    }
}
