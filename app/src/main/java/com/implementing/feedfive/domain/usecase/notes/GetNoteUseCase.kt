package com.implementing.feedfive.domain.usecase.notes

import com.implementing.feedfive.domain.repository.note.NoteRepository
import javax.inject.Inject

class GetNoteUseCase @Inject constructor(
    private val notesRepository: NoteRepository
) {
    suspend operator fun invoke(id: Int) = notesRepository.getNote(id)
}