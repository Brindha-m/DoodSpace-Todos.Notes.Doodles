package com.implementing.feedfive.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.implementing.feedfive.model.Bookmark
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {

    @Query("SELECT * FROM bookmarks")
    fun getAllBookmarks(): Flow<List<Bookmark>>
    @Query("SELECT * FROM bookmarks WHERE id = :id")
    suspend fun getBookmark(id: Int): Bookmark
    @Query("SELECT * FROM bookmarks WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' OR url LIKE '%' || :query || '%'")
    suspend fun getBookmark(query: String): List<Bookmark>
    @Insert
    suspend fun insertBookmark(bookmark: Bookmark)
    @Delete
    suspend fun deleteBookmark(bookmark: Bookmark)
    @Update
    suspend fun updateBookmark(bookmark: Bookmark)
    @Insert
    suspend fun insertBookmarks(bookmark: List<Bookmark>)

}