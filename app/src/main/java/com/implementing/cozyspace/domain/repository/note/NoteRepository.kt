package com.implementing.cozyspace.domain.repository.note

import com.implementing.cozyspace.model.Note
import com.implementing.cozyspace.model.NoteFolder
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    fun getAllFolderLessNotes(): Flow<List<Note>>

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
