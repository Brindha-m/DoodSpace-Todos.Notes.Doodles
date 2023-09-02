package com.implementing.cozyspace.model

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "note_folders")
data class NoteFolder(
    val name: String,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)