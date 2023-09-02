package com.implementing.cozyspace.domain.usecase.bookmark

import com.implementing.cozyspace.domain.repository.bookmark.BookmarkRepository
import com.implementing.cozyspace.model.Bookmark
import javax.inject.Inject

class AddBookmarkUsecase @Inject constructor(
    private val bookmarkRepository: BookmarkRepository
) {
    suspend operator fun invoke(bookmark: Bookmark) = bookmarkRepository.addBookmark(bookmark)
}