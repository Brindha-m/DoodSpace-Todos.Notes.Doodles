package com.implementing.cozyspace.domain.usecase.notes.notefolder

import com.implementing.cozyspace.domain.repository.note.NoteRepository
import com.implementing.cozyspace.model.NoteFolder
import javax.inject.Inject

class DeleteNoteFolderUseCass @Inject constructor(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(folder: NoteFolder) = noteRepository.deleteNoteFolder(folder)
}