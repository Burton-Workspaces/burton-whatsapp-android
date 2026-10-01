package com.burton.chat.core.data.local

import androidx.room.withTransaction
import com.burton.chat.core.common.CurrentUser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatDatabaseInitializer @Inject constructor(
    private val database: BurtonChatDatabase,
) {
    suspend fun seedIfNeeded() {
        if (database.contactDao().getById(CurrentUser.ID) != null) return
        val now = System.currentTimeMillis()
        database.withTransaction {
            database.contactDao().upsertAll(SeedData.contacts())
            database.conversationDao().upsertAll(SeedData.conversations(now))
            database.conversationMemberDao().upsertAll(SeedData.members())
            database.messageDao().upsertAll(SeedData.messages(now))
            SeedData.readState(now).forEach { database.readStateDao().upsert(it) }
        }
    }
}
