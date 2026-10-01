package com.burton.chat.feature.groups

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.burton.chat.core.domain.usecase.JoinGroupUseCase
import com.burton.chat.core.domain.usecase.ObserveDiscoverableGroupsUseCase
import com.burton.chat.core.domain.usecase.ObserveMyGroupsUseCase
import com.burton.chat.core.model.ConversationPreview
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GroupsUiState(
    val query: String = "",
    val myGroups: List<ConversationPreview> = emptyList(),
    val discoverable: List<ConversationPreview> = emptyList(),
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class GroupsViewModel @Inject constructor(
    observeMyGroups: ObserveMyGroupsUseCase,
    observeDiscoverable: ObserveDiscoverableGroupsUseCase,
    private val joinGroup: JoinGroupUseCase,
) : ViewModel() {
    private val query = MutableStateFlow("")

    val uiState: StateFlow<GroupsUiState> = query
        .flatMapLatest { currentQuery ->
            combine(
                observeMyGroups(currentQuery),
                observeDiscoverable(currentQuery),
            ) { mine, discoverable ->
                GroupsUiState(
                    query = currentQuery,
                    myGroups = mine,
                    discoverable = discoverable,
                )
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), GroupsUiState())

    fun onQueryChange(value: String) {
        query.value = value
    }

    fun join(groupId: String) {
        viewModelScope.launch {
            joinGroup(groupId)
        }
    }
}
