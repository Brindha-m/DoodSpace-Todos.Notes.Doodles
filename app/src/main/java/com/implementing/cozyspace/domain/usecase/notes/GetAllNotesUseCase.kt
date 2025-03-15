package com.implementing.cozyspace.domain.usecase.notes

import com.implementing.cozyspace.domain.repository.note.NoteRepository
import com.implementing.cozyspace.model.Note
import com.implementing.cozyspace.util.Order
import com.implementing.cozyspace.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class GetAllNotesUseCase @Inject constructor(
    private val notesRepository: NoteRepository,
) {
    operator fun invoke(order: Order) : Flow<List<Note>> {
        return notesRepository.getAllFolderLessNotes().map { list ->
            when (order.orderType) {
                is OrderType.ASC -> {
                    when (order) {
                        is Order.Alphabetical -> list.sortedWith(compareBy({!it.pinned}, { it.title }))
                        is Order.DateCreated -> list.sortedWith(compareBy({!it.pinned}, { it.createdDate }))
                        is Order.DateModified -> list.sortedWith(compareBy({!it.pinned}, { it.updatedDate }))
                        else -> list.sortedWith(compareBy({!it.pinned}, { it.updatedDate }))
                    }
                }
                is OrderType.DESC -> {
                    when (order) {
                        is Order.Alphabetical -> list.sortedWith(compareBy({it.pinned}, { it.title })).reversed()
                        is Order.DateCreated -> list.sortedWith(compareBy({it.pinned}, { it.createdDate })).reversed()
                        is Order.DateModified -> list.sortedWith(compareBy({it.pinned}, { it.updatedDate })).reversed()
                        else -> list.sortedWith(compareBy({it.pinned}, { it.updatedDate })).reversed()
                    }
                }
            }
        }
    }

}