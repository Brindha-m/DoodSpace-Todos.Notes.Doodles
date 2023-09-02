package com.implementing.cozyspace.domain.repository.note

import com.implementing.cozyspace.data.local.dao.NoteDao
import com.implementing.cozyspace.model.Note
import com.implementing.cozyspace.model.NoteFolder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class NoteRepositoryImpl(
    private val noteDao: NoteDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): NoteRepository {
    override fun getAllNotes(): Flow<List<Note>> {
        return noteDao.getAllNotes()
    }

    override suspend fun getNote(id: Int): Note {
        return withContext(ioDispatcher) {
            noteDao.getNote(id)
        }
    }

    override suspend fun searchNotes(query: String): List<Note> {
        return withContext(ioDispatcher) {
            noteDao.getNotesByTitle(query)
        }
    }

    override suspend fun addNote(note: Note) {
        return noteDao.insertNote(note)
    }

    override suspend fun updateNote(note: Note) {
        withContext(ioDispatcher) {
            noteDao.updateNote(note)
        }
    }

    override suspend fun deleteNote(note: Note) {
        withContext(ioDispatcher) {
            noteDao.deleteNote(note)
        }
    }

    override fun getNotesByFolder(folderId: Int): Flow<List<Note>> {
        return noteDao.getNotesByFolder(folderId)
    }


    override suspend fun insertNoteFolder(folder: NoteFolder) {
        withContext(ioDispatcher) {
            noteDao.insertNoteFolder(folder)
        }
    }

    override suspend fun updateNoteFolder(folder: NoteFolder) {
        withContext(ioDispatcher) {
            noteDao.updateNoteFolder(folder)
        }
    }

    override suspend fun deleteNoteFolder(folder: NoteFolder) {
        withContext(ioDispatcher) {
            noteDao.deleteNoteFolder(folder)
        }
    }

    override fun getAllNoteFolders(): Flow<List<NoteFolder>> {
        return noteDao.getAllNoteFolders()
    }

}