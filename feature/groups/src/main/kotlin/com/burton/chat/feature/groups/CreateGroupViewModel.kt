package com.burton.chat.feature.groups

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.burton.chat.core.domain.usecase.CreateGroupUseCase
import com.burton.chat.core.domain.usecase.ObserveContactsUseCase
import com.burton.chat.core.model.Contact
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CreateGroupUiState(
    val name: String = "",
    val contacts: List<Contact> = emptyList(),
    val selectedIds: Set<String> = emptySet(),
    val createdId: String? = null,
    val error: String? = null,
)

@HiltViewModel
class CreateGroupViewModel @Inject constructor(
    observeContacts: ObserveContactsUseCase,
    private val createGroup: CreateGroupUseCase,
) : ViewModel() {
    private val name = MutableStateFlow("")
    private val selectedIds = MutableStateFlow<Set<String>>(emptySet())
    private val createdId = MutableStateFlow<String?>(null)
    private val error = MutableStateFlow<String?>(null)

    val uiState: StateFlow<CreateGroupUiState> = combine(
        name,
        observeContacts(),
        selectedIds,
        createdId,
        error,
    ) { currentName, contacts, selected, created, currentError ->
        CreateGroupUiState(
            name = currentName,
            contacts = contacts,
            selectedIds = selected,
            createdId = created,
            error = currentError,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), CreateGroupUiState())

    fun onNameChange(value: String) {
        name.value = value
        error.value = null
    }

    fun toggleMember(contactId: String) {
        selectedIds.value = if (contactId in selectedIds.value) {
            selectedIds.value - contactId
        } else {
            selectedIds.value + contactId
        }
        error.value = null
    }

    fun create() {
        viewModelScope.launch {
            runCatching {
                createGroup(name.value, selectedIds.value)
            }.onSuccess { id ->
                createdId.value = id
            }.onFailure { throwable ->
                error.value = throwable.message ?: "Could not create group."
            }
        }
    }
}
