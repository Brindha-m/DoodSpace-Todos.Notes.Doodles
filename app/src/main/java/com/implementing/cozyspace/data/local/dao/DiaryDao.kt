package com.implementing.cozyspace.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.implementing.cozyspace.model.Diary
import kotlinx.coroutines.flow.Flow

@Dao
interface DiaryDao {
    @Query("SELECT * FROM diary")
    fun getAllEntries(): Flow<List<Diary>>
    @Query("SELECT * FROM diary WHERE id = :id")
    suspend fun getEntry(id: Int): Diary
    @Query("SELECT * FROM diary WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%'")
    suspend fun getEntriesByTitle(query: String) : List<Diary>
    @Insert
    suspend fun insertEntry(diary: Diary)
    @Insert
    suspend fun insertEntries(diary: List<Diary>)
    @Update
    suspend fun updateEntry(diary: Diary)
    @Delete
    suspend fun deleteEntry(diary: Diary)
}