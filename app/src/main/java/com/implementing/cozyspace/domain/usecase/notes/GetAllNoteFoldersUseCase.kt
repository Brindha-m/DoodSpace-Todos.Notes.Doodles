package com.implementing.cozyspace.domain.usecase.notes

import com.implementing.cozyspace.domain.repository.note.NoteRepository
import javax.inject.Inject

class GetAllNoteFoldersUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke() = repository.getAllNoteFolders()
}