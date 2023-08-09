package com.implementing.feedfive.inappscreens.bookmark

import com.implementing.feedfive.model.Bookmark
import com.implementing.feedfive.util.ItemView
import com.implementing.feedfive.util.Order

sealed class BookmarkEvent {
    data class AddBookmark(val bookmark: Bookmark) : BookmarkEvent()

    data class GetBookmark(val bookmarkId: Int) : BookmarkEvent()

    data class SearchBookmarks(val query: String) : BookmarkEvent()

    data class UpdateBookmark(val bookmark: Bookmark) : BookmarkEvent()

    data class DeleteBookmark(val bookmark: Bookmark) : BookmarkEvent()

    data class UpdateOrder(val order: Order) : BookmarkEvent()

    data class UpdateView(val view: ItemView) : BookmarkEvent()

    object ErrorDisplayed : BookmarkEvent()

}
