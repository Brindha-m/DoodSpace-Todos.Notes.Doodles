package com.implementing.cozyspace.domain.usecase.bookmark;

import com.implementing.cozyspace.domain.repository.bookmark.BookmarkRepository
import javax.inject.Inject;

class GetBookmarkUseCase @Inject constructor(
    private val bookmarkRepository: BookmarkRepository
) {
    suspend operator fun invoke(id: Int) = bookmarkRepository.getBookmark(id)

}