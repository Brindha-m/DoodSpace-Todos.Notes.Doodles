package com.implementing.cozyspace.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "notes",
    foreignKeys = [
        ForeignKey(
        entity = NoteFolder::class,
        parentColumns = ["id"],
        childColumns = ["folder_id"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.NO_ACTION
        )
    ]
)
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "folder_id")
    val folderId: Int? = null,

    val title: String = "",
    val content: String = "",

    @ColumnInfo(name = "created_date")
    val createdDate: Long = 0L,

    @ColumnInfo(name = "updated_date")
    val updatedDate: Long = 0L,

    val pinned: Boolean = false

)