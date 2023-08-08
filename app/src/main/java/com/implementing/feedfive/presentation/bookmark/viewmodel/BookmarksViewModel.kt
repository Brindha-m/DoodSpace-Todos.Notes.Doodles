package com.implementing.feedfive.presentation.bookmark.viewmodel

import androidx.lifecycle.ViewModel
import com.implementing.feedfive.domain.usecase.bookmark.AddBookmarkUsecase
import com.implementing.feedfive.domain.usecase.bookmark.DeleteBookmarkUseCase
import com.implementing.feedfive.domain.usecase.bookmark.GetAllBookmarksUseCase
import com.implementing.feedfive.domain.usecase.bookmark.GetBookmarkUseCase
import com.implementing.feedfive.domain.usecase.bookmark.SearchBookmarkUseCase
import com.implementing.feedfive.domain.usecase.bookmark.UpdateBookmarkUseCase
import com.implementing.feedfive.presentation.bookmark.BookmarkEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val addBookmark: AddBookmarkUsecase,
    private val updateBookmark: UpdateBookmarkUseCase,
    private val deleteBookmark: DeleteBookmarkUseCase,
    private val searchBookmarks: SearchBookmarkUseCase,
    private val getAddBookmarks: GetAllBookmarksUseCase,
    private val getBookmark: GetBookmarkUseCase

): ViewModel() {



    fun onEvent(addBookmark: BookmarkEvent) {

    }
}