package com.burton.chat.feature.contacts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.burton.chat.core.designsystem.component.EmptyState
import com.burton.chat.core.designsystem.component.InitialsAvatar
import com.burton.chat.core.designsystem.component.SearchField
import com.burton.chat.core.model.Contact

@Composable
fun ContactsRoute(
    onConversationOpened: (String) -> Unit,
    viewModel: ContactsViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(state.openedConversationId) {
        val id = state.openedConversationId ?: return@LaunchedEffect
        onConversationOpened(id)
        viewModel.consumeOpenedConversation()
    }
    ContactsScreen(
        state = state,
        onQueryChange = viewModel::onQueryChange,
        onContactClick = viewModel::openChat,
    )
}

@Composable
fun ContactsScreen(
    state: ContactsUiState,
    onQueryChange: (String) -> Unit,
    onContactClick: (String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        SearchField(
            query = state.query,
            onQueryChange = onQueryChange,
            placeholder = "Search contacts",
        )
        if (state.contacts.isEmpty()) {
            EmptyState(
                title = "No contacts",
                body = "People you can message will appear here.",
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.contacts, key = { it.id }) { contact ->
                    ContactRow(contact = contact, onClick = { onContactClick(contact.id) })
                    HorizontalDivider(modifier = Modifier.padding(start = 76.dp))
                }
            }
        }
    }
}

@Composable
private fun ContactRow(
    contact: Contact,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        InitialsAvatar(initials = contact.initials, seed = contact.avatarSeed)
        Column {
            Text(text = contact.displayName, style = MaterialTheme.typography.titleMedium)
            Text(
                text = contact.about,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
