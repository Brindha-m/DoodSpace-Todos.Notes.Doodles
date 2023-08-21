package com.implementing.feedfive.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.implementing.feedfive.model.Alarm
@Dao
interface AlarmDao {
    @Query("SELECT * FROM alarms")
    suspend fun getAll(): List<Alarm>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(alarm: Alarm)
    @Delete
    suspend fun delete(alarm: Alarm)
    @Query("DELETE FROM alarms WHERE id = :id")
    suspend fun delete(id: Int)
}