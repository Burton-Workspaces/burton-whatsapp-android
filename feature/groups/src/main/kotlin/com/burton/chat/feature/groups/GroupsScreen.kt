package com.burton.chat.feature.groups

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.burton.chat.core.designsystem.component.ConversationRow
import com.burton.chat.core.designsystem.component.EmptyState
import com.burton.chat.core.designsystem.component.SearchField
import com.burton.chat.core.model.ConversationPreview

@Composable
fun GroupsRoute(
    onGroupClick: (String) -> Unit,
    viewModel: GroupsViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    GroupsScreen(
        state = state,
        onQueryChange = viewModel::onQueryChange,
        onGroupClick = onGroupClick,
        onJoin = viewModel::join,
    )
}

@Composable
fun GroupsScreen(
    state: GroupsUiState,
    onQueryChange: (String) -> Unit,
    onGroupClick: (String) -> Unit,
    onJoin: (String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        SearchField(
            query = state.query,
            onQueryChange = onQueryChange,
            placeholder = "Search groups",
        )
        if (state.myGroups.isEmpty() && state.discoverable.isEmpty()) {
            EmptyState(
                title = "No groups",
                body = "Create a group or join one from Discover.",
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 88.dp),
            ) {
                if (state.myGroups.isNotEmpty()) {
                    item {
                        SectionHeader("Your groups")
                    }
                    items(state.myGroups, key = { it.id }) { group ->
                        ConversationRow(preview = group, onClick = { onGroupClick(group.id) })
                        HorizontalDivider(modifier = Modifier.padding(start = 76.dp))
                    }
                }
                if (state.discoverable.isNotEmpty()) {
                    item {
                        SectionHeader("Discover groups")
                    }
                    items(state.discoverable, key = { "discover-${it.id}" }) { group ->
                        DiscoverGroupRow(
                            preview = group,
                            onOpen = { onGroupClick(group.id) },
                            onJoin = { onJoin(group.id) },
                        )
                        HorizontalDivider(modifier = Modifier.padding(start = 76.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
    )
}

@Composable
private fun DiscoverGroupRow(
    preview: ConversationPreview,
    onOpen: () -> Unit,
    onJoin: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        ConversationRow(
            preview = preview,
            onClick = onOpen,
            modifier = Modifier.weight(1f),
        )
        Button(
            onClick = onJoin,
            modifier = Modifier.padding(end = 12.dp),
        ) {
            Text("Join")
        }
    }
}
