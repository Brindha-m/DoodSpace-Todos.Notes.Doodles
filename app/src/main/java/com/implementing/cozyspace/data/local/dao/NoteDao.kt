package com.implementing.cozyspace.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.implementing.cozyspace.model.Note
import com.implementing.cozyspace.model.NoteFolder
import kotlinx.coroutines.flow.Flow
@Dao
interface NoteDao {
    // Note section

    @Query("SELECT title, SUBSTR(content, 1, 200) AS content, created_date, updated_date, pinned, folder_id, id FROM notes WHERE folder_id IS NULL")
    fun getAllFolderLessNotes(): Flow<List<Note>>


    @Query("SELECT * FROM notes")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNote(id: Int): Note

    @Query("SELECT * FROM notes WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%'")
    suspend fun getNotesByTitle(query: String): List<Note>

// Use of conflict prevent from over writing
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNotes(notes: List<Note>)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)


    // Note Folder Section

    @Query("SELECT * FROM note_folders")
    fun getAllNoteFolders(): Flow<List<NoteFolder>>

    @Query("SELECT * FROM notes WHERE folder_id = :folderId")
    fun getNotesByFolder(folderId: Int): Flow<List<Note>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNoteFolder(folder: NoteFolder)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNoteFolders(folders: List<NoteFolder>)

    @Update
    suspend fun updateNoteFolder(folder: NoteFolder)

    @Delete
    suspend fun deleteNoteFolder(folder: NoteFolder)
}