package com.implementing.feedfive.model

import android.icu.text.CaseMap.Title
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.implementing.feedfive.util.Mood

@Entity(tableName = "diary")
data class Diary(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String = "",
    val content: String = "",
    @ColumnInfo(name = "created_date")
    val createdDate: Long = 0L,
    @ColumnInfo(name = "updated_date")
    val updatedDate: Long = 0L,
    val mood: Mood

)