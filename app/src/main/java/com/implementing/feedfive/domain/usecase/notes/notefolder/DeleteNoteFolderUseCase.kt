package com.implementing.feedfive.domain.usecase.notes.notefolder

import com.implementing.feedfive.domain.repository.note.NoteRepository
import com.implementing.feedfive.model.NoteFolder
import javax.inject.Inject

class DeleteNoteFolderUseCass @Inject constructor(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(folder: NoteFolder) = noteRepository.deleteNoteFolder(folder)
}