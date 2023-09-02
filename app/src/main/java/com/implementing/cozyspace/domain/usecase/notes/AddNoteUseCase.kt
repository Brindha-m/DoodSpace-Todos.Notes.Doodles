package com.implementing.cozyspace.domain.usecase.notes

import com.implementing.cozyspace.domain.repository.note.NoteRepository
import com.implementing.cozyspace.model.Note
import javax.inject.Inject

class AddNoteUseCase @Inject constructor(
    private val notesRepository: NoteRepository
) {
    suspend operator fun invoke(note: Note) = notesRepository.addNote(note)
}