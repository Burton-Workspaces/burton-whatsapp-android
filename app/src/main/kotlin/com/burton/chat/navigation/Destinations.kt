package com.burton.chat.navigation

object Destinations {
    const val HOME = "home"
    const val CONVERSATION = "conversation/{conversationId}"
    const val CREATE_GROUP = "create_group"
    const val GROUP_DETAILS = "group/{groupId}"

    fun conversation(id: String) = "conversation/$id"
    fun groupDetails(id: String) = "group/$id"
}
