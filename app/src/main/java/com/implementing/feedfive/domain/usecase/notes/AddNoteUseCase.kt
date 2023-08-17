package com.implementing.feedfive.domain.usecase.notes

import com.implementing.feedfive.domain.repository.note.NoteRepository
import com.implementing.feedfive.model.Note
import javax.inject.Inject

class AddNoteUseCase @Inject constructor(
    private val notesRepository: NoteRepository
) {
    suspend operator fun invoke(note: Note) = notesRepository.addNote(note)
}