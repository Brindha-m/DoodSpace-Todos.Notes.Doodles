package com.implementing.feedfive.domain.usecase.bookmark;

import com.implementing.feedfive.domain.repository.bookmark.BookmarkRepository
import javax.inject.Inject;

class GetBookmarkUseCase @Inject constructor(
    private val bookmarkRepository: BookmarkRepository
) {
    suspend operator fun invoke(id: Int) = bookmarkRepository.getBookmark(id)

}