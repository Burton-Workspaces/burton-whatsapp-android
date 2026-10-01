package com.burton.chat.feature.chats

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.burton.chat.core.designsystem.component.ConversationRow
import com.burton.chat.core.designsystem.component.EmptyState
import com.burton.chat.core.designsystem.component.SearchField

@Composable
fun ChatsRoute(
    onConversationClick: (String) -> Unit,
    viewModel: ChatsViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    ChatsScreen(
        state = state,
        onQueryChange = viewModel::onQueryChange,
        onConversationClick = onConversationClick,
    )
}

@Composable
fun ChatsScreen(
    state: ChatsUiState,
    onQueryChange: (String) -> Unit,
    onConversationClick: (String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        SearchField(
            query = state.query,
            onQueryChange = onQueryChange,
            placeholder = "Search chats",
        )
        if (state.chats.isEmpty()) {
            EmptyState(
                title = "No chats yet",
                body = "Start a conversation from Contacts, or join a group.",
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.chats, key = { it.id }) { chat ->
                    ConversationRow(
                        preview = chat,
                        onClick = { onConversationClick(chat.id) },
                    )
                    HorizontalDivider(modifier = Modifier.padding(start = 76.dp))
                }
            }
        }
    }
}
