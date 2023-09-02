package com.implementing.cozyspace.inappscreens.bookmark

import com.implementing.cozyspace.model.Bookmark
import com.implementing.cozyspace.util.ItemView
import com.implementing.cozyspace.util.Order

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
