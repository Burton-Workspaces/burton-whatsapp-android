package com.burton.chat.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.burton.chat.core.data.local.dao.ContactDao
import com.burton.chat.core.data.local.dao.ConversationDao
import com.burton.chat.core.data.local.dao.ConversationMemberDao
import com.burton.chat.core.data.local.dao.MessageDao
import com.burton.chat.core.data.local.dao.ReadStateDao
import com.burton.chat.core.data.local.entity.ContactEntity
import com.burton.chat.core.data.local.entity.ConversationEntity
import com.burton.chat.core.data.local.entity.ConversationMemberEntity
import com.burton.chat.core.data.local.entity.MessageEntity
import com.burton.chat.core.data.local.entity.ReadStateEntity

@Database(
    entities = [
        ContactEntity::class,
        ConversationEntity::class,
        ConversationMemberEntity::class,
        MessageEntity::class,
        ReadStateEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class BurtonChatDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao
    abstract fun conversationDao(): ConversationDao
    abstract fun conversationMemberDao(): ConversationMemberDao
    abstract fun messageDao(): MessageDao
    abstract fun readStateDao(): ReadStateDao
}
