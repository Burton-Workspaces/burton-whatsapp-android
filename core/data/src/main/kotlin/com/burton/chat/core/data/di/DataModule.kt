package com.burton.chat.core.data.di

import android.content.Context
import androidx.room.Room
import com.burton.chat.core.data.local.BurtonChatDatabase
import com.burton.chat.core.data.local.dao.ContactDao
import com.burton.chat.core.data.local.dao.ConversationDao
import com.burton.chat.core.data.local.dao.ConversationMemberDao
import com.burton.chat.core.data.local.dao.MessageDao
import com.burton.chat.core.data.local.dao.ReadStateDao
import com.burton.chat.core.data.repository.ContactRepositoryImpl
import com.burton.chat.core.data.repository.ConversationRepositoryImpl
import com.burton.chat.core.data.repository.MessageRepositoryImpl
import com.burton.chat.core.domain.repository.ContactRepository
import com.burton.chat.core.domain.repository.ConversationRepository
import com.burton.chat.core.domain.repository.MessageRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindContactRepository(impl: ContactRepositoryImpl): ContactRepository

    @Binds
    abstract fun bindConversationRepository(impl: ConversationRepositoryImpl): ConversationRepository

    @Binds
    abstract fun bindMessageRepository(impl: MessageRepositoryImpl): MessageRepository
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): BurtonChatDatabase =
        Room.databaseBuilder(context, BurtonChatDatabase::class.java, "burton-chat.db")
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()

    @Provides
    fun provideContactDao(database: BurtonChatDatabase): ContactDao = database.contactDao()

    @Provides
    fun provideConversationDao(database: BurtonChatDatabase): ConversationDao =
        database.conversationDao()

    @Provides
    fun provideConversationMemberDao(database: BurtonChatDatabase): ConversationMemberDao =
        database.conversationMemberDao()

    @Provides
    fun provideMessageDao(database: BurtonChatDatabase): MessageDao = database.messageDao()

    @Provides
    fun provideReadStateDao(database: BurtonChatDatabase): ReadStateDao = database.readStateDao()

    @Provides
    @Singleton
    @ApplicationScope
    fun provideApplicationScope(): CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Default)
}
