package com.implementing.feedfive.model

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "note_folders")
data class NoteFolder (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String
)