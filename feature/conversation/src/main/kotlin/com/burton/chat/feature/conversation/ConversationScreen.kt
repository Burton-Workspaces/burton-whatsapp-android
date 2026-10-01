package com.burton.chat.feature.conversation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import com.burton.chat.core.common.DateTimeUi
import com.burton.chat.core.designsystem.theme.ChatBackground
import com.burton.chat.core.designsystem.theme.Forest
import com.burton.chat.core.designsystem.theme.IncomingBubble
import com.burton.chat.core.designsystem.theme.OnForest
import com.burton.chat.core.designsystem.theme.OutgoingBubble
import com.burton.chat.core.model.ConversationType
import com.burton.chat.core.model.Message
import com.burton.chat.core.model.MessageStatus

@Composable
fun ConversationRoute(
    onBack: () -> Unit,
    onGroupInfo: (String) -> Unit,
    viewModel: ConversationViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    ConversationScreen(
        state = state,
        onBack = onBack,
        onGroupInfo = onGroupInfo,
        onDraftChange = viewModel::onDraftChange,
        onSend = viewModel::send,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationScreen(
    state: ConversationUiState,
    onBack: () -> Unit,
    onGroupInfo: (String) -> Unit,
    onDraftChange: (String) -> Unit,
    onSend: () -> Unit,
) {
    val details = state.details
    val listState = rememberLazyListState()
    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            listState.scrollToItem(state.messages.lastIndex)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(
                        modifier = Modifier.clickable(enabled = details?.type == ConversationType.GROUP) {
                            details?.let { onGroupInfo(it.id) }
                        },
                    ) {
                        Text(details?.title ?: "Chat")
                        val subtitle = when {
                            details == null -> ""
                            details.type == ConversationType.GROUP -> "${details.memberCount} members"
                            else -> details.directPeer?.about ?: details.directPeer?.phoneNumber.orEmpty()
                        }
                        if (subtitle.isNotBlank()) {
                            Text(
                                text = subtitle,
                                style = MaterialTheme.typography.labelSmall,
                                color = OnForest.copy(alpha = 0.85f),
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (details?.type == ConversationType.GROUP) {
                        IconButton(onClick = { details.let { onGroupInfo(it.id) } }) {
                            Icon(Icons.Filled.Groups, contentDescription = "Group info")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Forest,
                    titleContentColor = OnForest,
                    navigationIconContentColor = OnForest,
                    actionIconContentColor = OnForest,
                ),
            )
        },
        bottomBar = {
            if (details?.isMember == true) {
                MessageComposer(
                    draft = state.draft,
                    onDraftChange = onDraftChange,
                    onSend = onSend,
                )
            } else {
                Surface(tonalElevation = 2.dp) {
                    Text(
                        text = "Join this group to send messages.",
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(ChatBackground),
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(state.messages, key = { it.id }) { message ->
                    MessageBubble(
                        message = message,
                        showSender = details?.type == ConversationType.GROUP && !message.isMine,
                    )
                }
            }
        }
    }
}

@Composable
private fun MessageBubble(
    message: Message,
    showSender: Boolean,
) {
    val mine = message.isMine
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (mine) Arrangement.End else Arrangement.Start,
    ) {
        Surface(
            color = if (mine) OutgoingBubble else IncomingBubble,
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (mine) 16.dp else 4.dp,
                bottomEnd = if (mine) 4.dp else 16.dp,
            ),
            shadowElevation = 1.dp,
            modifier = Modifier.widthIn(max = 320.dp),
        ) {
            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                if (showSender) {
                    Text(
                        text = message.sender.displayName,
                        style = MaterialTheme.typography.labelSmall,
                        color = Forest,
                    )
                }
                Text(text = message.body, style = MaterialTheme.typography.bodyLarge)
                Row(
                    modifier = Modifier.align(Alignment.End),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = DateTimeUi.messageTimestamp(message.createdAt),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    if (mine) {
                        Icon(
                            imageVector = if (message.status == MessageStatus.READ ||
                                message.status == MessageStatus.DELIVERED
                            ) {
                                Icons.Filled.DoneAll
                            } else {
                                Icons.Filled.Done
                            },
                            contentDescription = message.status.name,
                            tint = if (message.status == MessageStatus.READ) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MessageComposer(
    draft: String,
    onDraftChange: (String) -> Unit,
    onSend: () -> Unit,
) {
    Surface(tonalElevation = 3.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .imePadding()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            OutlinedTextField(
                value = draft,
                onValueChange = onDraftChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Message") },
                shape = RoundedCornerShape(24.dp),
                maxLines = 4,
            )
            FilledIconButton(
                onClick = onSend,
                enabled = draft.isNotBlank(),
                shape = CircleShape,
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
            }
        }
    }
}
