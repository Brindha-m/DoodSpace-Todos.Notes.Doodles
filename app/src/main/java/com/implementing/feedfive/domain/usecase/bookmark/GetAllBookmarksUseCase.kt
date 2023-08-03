package com.implementing.feedfive.domain.usecase.bookmark

import com.implementing.feedfive.domain.repository.bookmark.BookmarkRepository
import javax.inject.Inject

class GetAllBookmarksUseCase @Inject constructor(
    private val bookmarkRepository: BookmarkRepository
) {
    operator fun invoke() = bookmarkRepository.getAllBookmarks()
}