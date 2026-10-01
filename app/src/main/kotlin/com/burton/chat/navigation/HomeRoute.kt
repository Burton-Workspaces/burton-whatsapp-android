package com.burton.chat.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.burton.chat.core.designsystem.theme.Forest
import com.burton.chat.core.designsystem.theme.OnForest

enum class HomeTab(val label: String, val icon: ImageVector) {
    Chats("Chats", Icons.AutoMirrored.Filled.Chat),
    Groups("Groups", Icons.Filled.Groups),
    Contacts("Contacts", Icons.Filled.Person),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeRoute(
    onCreateGroup: () -> Unit,
    chatsContent: @Composable () -> Unit,
    groupsContent: @Composable () -> Unit,
    contactsContent: @Composable () -> Unit,
) {
    var tab by rememberSaveable { mutableStateOf(HomeTab.Chats) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Burton") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Forest,
                    titleContentColor = OnForest,
                    navigationIconContentColor = OnForest,
                ),
            )
        },
        bottomBar = {
            NavigationBar {
                HomeTab.entries.forEach { destination ->
                    NavigationBarItem(
                        selected = tab == destination,
                        onClick = { tab = destination },
                        icon = { Icon(destination.icon, contentDescription = destination.label) },
                        label = { Text(destination.label) },
                    )
                }
            }
        },
        floatingActionButton = {
            if (tab == HomeTab.Groups) {
                FloatingActionButton(onClick = onCreateGroup) {
                    Icon(Icons.Filled.Add, contentDescription = "Create group")
                }
            }
        },
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (tab) {
                HomeTab.Chats -> chatsContent()
                HomeTab.Groups -> groupsContent()
                HomeTab.Contacts -> contactsContent()
            }
        }
    }
}
