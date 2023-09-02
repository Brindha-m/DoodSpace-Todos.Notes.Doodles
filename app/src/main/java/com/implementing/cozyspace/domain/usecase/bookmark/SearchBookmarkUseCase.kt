package com.implementing.cozyspace.domain.usecase.bookmark

import com.implementing.cozyspace.domain.repository.bookmark.BookmarkRepository
import javax.inject.Inject

class SearchBookmarkUseCase @Inject constructor(
    private val bookmarkRepository: BookmarkRepository
) {
    suspend operator fun invoke(query: String) = bookmarkRepository.getBookmark(query)

}