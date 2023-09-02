package com.implementing.cozyspace.domain.usecase.diary

import com.implementing.cozyspace.domain.repository.diary.DiaryRepository
import com.implementing.cozyspace.model.Diary
import com.implementing.cozyspace.util.Order
import com.implementing.cozyspace.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


import javax.inject.Inject

class GetAllDiaryEntryUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository
) {
    operator fun invoke(order: Order) : Flow<List<Diary>> {
        return diaryRepository.getAllEntries().map { entries ->
            when (order.orderType) {
                is OrderType.ASC -> {
                    when (order) {
                        is Order.Alphabetical -> entries.sortedBy { it.title }
                        is Order.DateCreated -> entries.sortedBy { it.createdDate }
                        is Order.DateModified -> entries.sortedBy { it.updatedDate }
                        else -> entries.sortedBy { it.updatedDate }
                    }
                }
                is OrderType.DESC -> {
                    when (order) {
                        is Order.Alphabetical -> entries.sortedByDescending { it.title }
                        is Order.DateCreated -> entries.sortedByDescending { it.createdDate }
                        is Order.DateModified -> entries.sortedByDescending { it.updatedDate }
                        else -> entries.sortedByDescending { it.updatedDate }
                    }
                }
            }
        }
    }
}