package com.implementing.cozyspace.domain.usecase.notes

import com.implementing.cozyspace.domain.repository.note.NoteRepository
import com.implementing.cozyspace.model.Note
import javax.inject.Inject

class UpdateNoteUseCase @Inject constructor(
    private val notesRepository: NoteRepository
) {
    suspend operator fun invoke(note: Note) = notesRepository.updateNote(note)
}