package com.implementing.feedfive.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.implementing.feedfive.model.Task
import dagger.multibindings.IntoSet
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTask(id: Int): Task

    @Query("SELECT * FROM tasks WHERE title LIKE '%' || :title || '%'")
    fun getTasksByTitle(title: String): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTasks(task: List<Task>)

    @Delete
    suspend fun deleteTask(task: Task)
    @Update
    suspend fun updateTask(task: Task)

    @Query("UPDATE tasks SET is_completed = :completed WHERE id = :id")
        suspend fun updateComplete(id: Int, completed: Boolean)

}