package com.burton.chat.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.burton.chat.feature.chats.ChatsRoute
import com.burton.chat.feature.contacts.ContactsRoute
import com.burton.chat.feature.conversation.ConversationRoute
import com.burton.chat.feature.groups.CreateGroupRoute
import com.burton.chat.feature.groups.GroupDetailsRoute
import com.burton.chat.feature.groups.GroupsRoute

@Composable
fun BurtonChatApp() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Destinations.HOME,
        modifier = Modifier.fillMaxSize(),
    ) {
        composable(Destinations.HOME) {
            HomeRoute(
                onCreateGroup = { navController.navigate(Destinations.CREATE_GROUP) },
                chatsContent = {
                    ChatsRoute(
                        onConversationClick = { id ->
                            navController.navigate(Destinations.conversation(id))
                        },
                    )
                },
                groupsContent = {
                    GroupsRoute(
                        onGroupClick = { id ->
                            navController.navigate(Destinations.conversation(id))
                        },
                    )
                },
                contactsContent = {
                    ContactsRoute(
                        onConversationOpened = { id ->
                            navController.navigate(Destinations.conversation(id))
                        },
                    )
                },
            )
        }
        composable(
            route = Destinations.CONVERSATION,
            arguments = listOf(navArgument("conversationId") { type = NavType.StringType }),
        ) {
            ConversationRoute(
                onBack = { navController.popBackStack() },
                onGroupInfo = { id -> navController.navigate(Destinations.groupDetails(id)) },
            )
        }
        composable(Destinations.CREATE_GROUP) {
            CreateGroupRoute(
                onBack = { navController.popBackStack() },
                onCreated = { id ->
                    navController.popBackStack()
                    navController.navigate(Destinations.conversation(id))
                },
            )
        }
        composable(
            route = Destinations.GROUP_DETAILS,
            arguments = listOf(navArgument("groupId") { type = NavType.StringType }),
        ) {
            GroupDetailsRoute(
                onBack = { navController.popBackStack() },
                onOpenChat = { id ->
                    navController.popBackStack()
                    navController.navigate(Destinations.conversation(id))
                },
            )
        }
    }
}
