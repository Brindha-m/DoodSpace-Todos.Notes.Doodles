package com.implementing.cozyspace.domain.repository.bookmark

import com.implementing.cozyspace.model.Bookmark
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {

    fun getAllBookmarks(): Flow<List<Bookmark>>

    suspend fun getBookmark(id: Int): Bookmark

    suspend fun getBookmark(query: String): List<Bookmark>

    suspend fun addBookmark(bookmark: Bookmark)

    suspend fun deleteBookmark(bookmark: Bookmark)

    suspend fun updateBookmark(bookmark: Bookmark)
}