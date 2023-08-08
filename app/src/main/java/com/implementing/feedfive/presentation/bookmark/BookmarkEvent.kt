package com.implementing.feedfive.presentation.bookmark

import androidx.room.Index
import com.implementing.feedfive.model.Bookmark
import com.implementing.feedfive.util.ItemView
import com.implementing.feedfive.util.Order

sealed class BookmarkEvent {
    data class AddBookmark(val bookmark: Bookmark) : BookmarkEvent()

    data class GetBookmark(val bookmark: Int) : BookmarkEvent()

    data class SearchBookmarks(val query: String) : BookmarkEvent()

    data class UpdateBookmark(val bookmark: Bookmark) : BookmarkEvent()

    data class DeleteBookmark(val bookmark: Bookmark) : BookmarkEvent()

    data class UpdateOrder(val order: Order) : BookmarkEvent()

    data class UpdateView(val view: ItemView) : BookmarkEvent()

    object ErrorDisplayed : BookmarkEvent()

}
