package com.implementing.feedfive.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task (
    val title: String,
    val description: String = "",
    val priority: Int = 0,
    val dueDate: Long = 0L,

    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean = false,

    @ColumnInfo(name = "created_date")
    val createdDate: Long = 0L,

    @ColumnInfo(name = "updated_date")
    val updatedDate: Long = 0L,

    @ColumnInfo(name = "sub_tasks")
    val subTasks: List<SubTask> = emptyList(),

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0

)