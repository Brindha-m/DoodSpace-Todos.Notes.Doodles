package com.implementing.cozyspace.domain.usecase.notes

import com.implementing.cozyspace.domain.repository.note.NoteRepository
import javax.inject.Inject

class GetNoteUseCase @Inject constructor(
    private val notesRepository: NoteRepository
) {
    suspend operator fun invoke(id: Int) = notesRepository.getNote(id)
}