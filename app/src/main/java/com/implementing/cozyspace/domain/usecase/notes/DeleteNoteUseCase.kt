package com.implementing.cozyspace.domain.usecase.notes

import com.implementing.cozyspace.domain.repository.note.NoteRepository
import com.implementing.cozyspace.model.Note
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note) = repository.deleteNote(note)
}