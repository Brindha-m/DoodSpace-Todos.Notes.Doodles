package com.implementing.feedfive.domain.usecase.bookmark

import com.implementing.feedfive.domain.repository.bookmark.BookmarkRepository
import com.implementing.feedfive.model.Bookmark
import javax.inject.Inject

class UpdateBookmarkUseCase @Inject constructor(
    private val bookmarkRepository: BookmarkRepository
){
    suspend operator fun invoke(bookmark: Bookmark) = bookmarkRepository.updateBookmark(bookmark)
}