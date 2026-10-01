package com.burton.chat.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.burton.chat.core.data.local.entity.ReadStateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReadStateDao {
    @Query("SELECT * FROM read_state")
    fun observeAll(): Flow<List<ReadStateEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(state: ReadStateEntity)
}
