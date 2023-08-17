package com.implementing.feedfive.domain.repository.note

import com.implementing.feedfive.model.Note
import com.implementing.feedfive.model.NoteFolder
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getAllNotes(): Flow<List<Note>>

    suspend fun getNote(id: Int): Note

    suspend fun searchNotes(query: String): List<Note>

    suspend fun addNote(note: Note)

    suspend fun updateNote(note: Note)

    suspend fun deleteNote(note: Note)

    fun getNotesByFolder(folderId: Int): Flow<List<Note>>

    fun getAllNoteFolders(): Flow<List<NoteFolder>>
    suspend fun insertNoteFolder(folder: NoteFolder)

    suspend fun updateNoteFolder(folder: NoteFolder)

    suspend fun deleteNoteFolder(folder: NoteFolder)

}
