package com.implementing.feedfive.domain.repository.bookmark

import android.view.KeyEvent.DispatcherState
import com.implementing.feedfive.data.local.dao.BookmarkDao
import com.implementing.feedfive.model.Bookmark
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class BookmarkRepositoryImpl(
    private val bookmarkDao: BookmarkDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): BookmarkRepository {
    override fun getAllBookmarks(): Flow<List<Bookmark>> {
        return bookmarkDao.getAllBookmarks()
    }

    override suspend fun getBookmark(id: Int): Bookmark {
        return withContext(ioDispatcher) {
            bookmarkDao.getBookmark(id)
        }
    }

    override suspend fun getBookmark(query: String): List<Bookmark> {
        return withContext(ioDispatcher){
            // it runs on io thread instead of main thread. Preventing UI freezes and keeping the app responsive.
            bookmarkDao.getBookmark(query)
        }
    }

    override suspend fun addBookmark(bookmark: Bookmark) {
        return bookmarkDao.insertBookmark(bookmark)
    }

    override suspend fun deleteBookmark(bookmark: Bookmark) {
        return bookmarkDao.deleteBookmark(bookmark)
    }

    override suspend fun updateBookmark(bookmark: Bookmark) {
        return bookmarkDao.updateBookmark(bookmark)
    }

}