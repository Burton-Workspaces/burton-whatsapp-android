package com.burton.chat.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.burton.chat.core.data.local.entity.ConversationMemberEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversationMemberDao {
    @Query("SELECT * FROM conversation_members")
    fun observeAll(): Flow<List<ConversationMemberEntity>>

    @Query("SELECT * FROM conversation_members WHERE conversationId = :conversationId")
    fun observeForConversation(conversationId: String): Flow<List<ConversationMemberEntity>>

    @Query("SELECT * FROM conversation_members WHERE conversationId = :conversationId")
    suspend fun getForConversation(conversationId: String): List<ConversationMemberEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(members: List<ConversationMemberEntity>)

    @Query(
        "DELETE FROM conversation_members WHERE conversationId = :conversationId AND contactId = :contactId",
    )
    suspend fun remove(conversationId: String, contactId: String)

    @Query(
        """
        SELECT EXISTS(
            SELECT 1 FROM conversation_members
            WHERE conversationId = :conversationId AND contactId = :contactId
        )
        """,
    )
    suspend fun isMember(conversationId: String, contactId: String): Boolean
}
