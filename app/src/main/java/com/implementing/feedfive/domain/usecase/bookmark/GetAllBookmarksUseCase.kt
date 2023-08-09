package com.implementing.feedfive.domain.usecase.bookmark

import com.implementing.feedfive.domain.repository.bookmark.BookmarkRepository
import com.implementing.feedfive.model.Bookmark
import com.implementing.feedfive.util.Order
import com.implementing.feedfive.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllBookmarksUseCase @Inject constructor(
    private val bookmarksRepository: BookmarkRepository
) {
    operator fun invoke(order: Order) : Flow<List<Bookmark>>{
        return bookmarksRepository.getAllBookmarks().map { bookmarks ->
            when (order.orderType) {
                is OrderType.ASC -> {
                    when (order) {
                        is Order.Alphabetical -> bookmarks.sortedBy { it.title }
                        is Order.DateCreated -> bookmarks.sortedBy { it.createdDate }
                        is Order.DateModified -> bookmarks.sortedBy { it.updatedDate }
                        else -> bookmarks.sortedBy { it.updatedDate }
                    }
                }
                is OrderType.DESC -> {
                    when (order) {
                        is Order.Alphabetical -> bookmarks.sortedByDescending { it.title }
                        is Order.DateCreated -> bookmarks.sortedByDescending { it.createdDate }
                        is Order.DateModified -> bookmarks.sortedByDescending { it.updatedDate }
                        else -> bookmarks.sortedByDescending { it.updatedDate }
                    }
                }
            }
        }
    }
}