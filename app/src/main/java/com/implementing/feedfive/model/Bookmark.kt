package com.implementing.feedfive.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.Date

@Entity(tableName = "bookmarks")
data class Bookmark(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val url: String = "",
    val title: String? = "",
    val description: String = "",
    @ColumnInfo(name = "created_date")
    val createdDate: Date = Date(),
    @ColumnInfo(name = "updated_date")
    val updatedDate: Date = Date()

)
