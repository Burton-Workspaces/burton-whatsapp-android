package com.burton.chat.feature.groups

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.burton.chat.core.designsystem.component.InitialsAvatar
import com.burton.chat.core.designsystem.theme.Forest
import com.burton.chat.core.designsystem.theme.OnForest
import com.burton.chat.core.model.Contact

@Composable
fun GroupDetailsRoute(
    onBack: () -> Unit,
    onOpenChat: (String) -> Unit,
    viewModel: GroupDetailsViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(state.left) {
        if (state.left) onBack()
    }
    GroupDetailsScreen(
        state = state,
        onBack = onBack,
        onOpenChat = onOpenChat,
        onJoin = viewModel::join,
        onLeave = viewModel::leave,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDetailsScreen(
    state: GroupDetailsUiState,
    onBack: () -> Unit,
    onOpenChat: (String) -> Unit,
    onJoin: () -> Unit,
    onLeave: () -> Unit,
) {
    val details = state.details
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(details?.title ?: "Group") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Forest,
                    titleContentColor = OnForest,
                    navigationIconContentColor = OnForest,
                ),
            )
        },
        bottomBar = {
            if (details != null) {
                if (details.isMember) {
                    OutlinedButton(
                        onClick = onLeave,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    ) {
                        Text("Leave group")
                    }
                } else {
                    Button(
                        onClick = onJoin,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    ) {
                        Text("Join group")
                    }
                }
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            Text(
                text = "${details?.memberCount ?: 0} members",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(16.dp),
            )
            if (details?.isMember == true) {
                Button(
                    onClick = { details.let { onOpenChat(it.id) } },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                ) {
                    Text("Open chat")
                }
            }
            LazyColumn(modifier = Modifier.padding(top = 8.dp)) {
                items(details?.members.orEmpty(), key = { it.id }) { member ->
                    MemberRow(member)
                }
            }
        }
    }
}

@Composable
private fun MemberRow(contact: Contact) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        InitialsAvatar(initials = contact.initials, seed = contact.avatarSeed, size = 40.dp)
        Column {
            Text(
                text = if (contact.isMe) "${contact.displayName} (you)" else contact.displayName,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = contact.about,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
