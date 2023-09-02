package com.implementing.cozyspace.domain.usecase.notes

import com.implementing.cozyspace.domain.repository.note.NoteRepository
import javax.inject.Inject

class SearchNotesUseCase @Inject constructor(
    private val notesRepository: NoteRepository
) {
    suspend operator fun invoke(query: String) = notesRepository.searchNotes(query)
}