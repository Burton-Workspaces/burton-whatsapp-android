package com.burton.chat.feature.groups

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.burton.chat.core.domain.usecase.JoinGroupUseCase
import com.burton.chat.core.domain.usecase.LeaveGroupUseCase
import com.burton.chat.core.domain.usecase.ObserveConversationUseCase
import com.burton.chat.core.model.ConversationDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GroupDetailsUiState(
    val details: ConversationDetails? = null,
    val left: Boolean = false,
)

@HiltViewModel
class GroupDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    observeConversation: ObserveConversationUseCase,
    private val joinGroup: JoinGroupUseCase,
    private val leaveGroup: LeaveGroupUseCase,
) : ViewModel() {
    private val groupId: String = checkNotNull(savedStateHandle["groupId"])
    private val left = MutableStateFlow(false)

    val uiState: StateFlow<GroupDetailsUiState> = combine(
        observeConversation(groupId),
        left,
    ) { details, hasLeft ->
        GroupDetailsUiState(details = details, left = hasLeft)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), GroupDetailsUiState())

    fun join() {
        viewModelScope.launch { joinGroup(groupId) }
    }

    fun leave() {
        viewModelScope.launch {
            leaveGroup(groupId)
            left.value = true
        }
    }
}
