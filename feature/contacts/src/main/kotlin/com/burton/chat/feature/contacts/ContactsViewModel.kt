package com.burton.chat.feature.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.burton.chat.core.domain.usecase.GetOrCreateDirectConversationUseCase
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

data class ContactsUiState(
    val query: String = "",
    val contacts: List<Contact> = emptyList(),
    val openedConversationId: String? = null,
)

@HiltViewModel
class ContactsViewModel @Inject constructor(
    observeContacts: ObserveContactsUseCase,
    private val getOrCreateDirectConversation: GetOrCreateDirectConversationUseCase,
) : ViewModel() {
    private val query = MutableStateFlow("")
    private val openedConversationId = MutableStateFlow<String?>(null)

    val uiState: StateFlow<ContactsUiState> = combine(
        query,
        observeContacts(),
        openedConversationId,
    ) { currentQuery, contacts, openedId ->
        val filtered = if (currentQuery.isBlank()) {
            contacts
        } else {
            contacts.filter {
                it.displayName.contains(currentQuery, ignoreCase = true) ||
                    it.phoneNumber.contains(currentQuery, ignoreCase = true)
            }
        }
        ContactsUiState(query = currentQuery, contacts = filtered, openedConversationId = openedId)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ContactsUiState())

    fun onQueryChange(value: String) {
        query.value = value
    }

    fun openChat(contactId: String) {
        viewModelScope.launch {
            openedConversationId.value = getOrCreateDirectConversation(contactId)
        }
    }

    fun consumeOpenedConversation() {
        openedConversationId.value = null
    }
}
