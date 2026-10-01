package com.burton.chat.feature.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.burton.chat.core.domain.usecase.ObserveChatsUseCase
import com.burton.chat.core.model.ConversationPreview
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class ChatsUiState(
    val query: String = "",
    val chats: List<ConversationPreview> = emptyList(),
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ChatsViewModel @Inject constructor(
    observeChats: ObserveChatsUseCase,
) : ViewModel() {
    private val query = MutableStateFlow("")

    val uiState: StateFlow<ChatsUiState> = query
        .flatMapLatest { currentQuery ->
            observeChats(currentQuery).map { chats ->
                ChatsUiState(query = currentQuery, chats = chats)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ChatsUiState())

    fun onQueryChange(value: String) {
        query.value = value
    }
}
