package com.implementing.feedfive.domain.usecase.notes

import com.implementing.feedfive.domain.repository.note.NoteRepository
import com.implementing.feedfive.model.Note
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note) = repository.deleteNote(note)
}