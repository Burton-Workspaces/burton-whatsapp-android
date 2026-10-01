package com.burton.chat.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.burton.chat.core.data.local.entity.ConversationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversationDao {
    @Query("SELECT * FROM conversations ORDER BY lastMessageAt DESC")
    fun observeAll(): Flow<List<ConversationEntity>>

    @Query("SELECT * FROM conversations WHERE id = :id")
    fun observeById(id: String): Flow<ConversationEntity?>

    @Query("SELECT * FROM conversations WHERE id = :id")
    suspend fun getById(id: String): ConversationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(conversation: ConversationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(conversations: List<ConversationEntity>)

    @Query("UPDATE conversations SET lastMessageAt = :timestamp WHERE id = :id")
    suspend fun updateLastMessageAt(id: String, timestamp: Long)

    @Query(
        """
        SELECT c.* FROM conversations c
        INNER JOIN conversation_members a ON a.conversationId = c.id AND a.contactId = :firstId
        INNER JOIN conversation_members b ON b.conversationId = c.id AND b.contactId = :secondId
        WHERE c.type = 'DIRECT'
        LIMIT 1
        """,
    )
    suspend fun findDirectBetween(firstId: String, secondId: String): ConversationEntity?
}
