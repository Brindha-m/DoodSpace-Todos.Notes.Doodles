package com.implementing.feedfive.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.implementing.feedfive.data.local.dao.BookmarkDao
import com.implementing.feedfive.model.Bookmark
@AutoMigration(from = 1, to = 2)
@Database(version = 2, entities = [Bookmark::class], exportSchema = false)
abstract class FeedFiveDatabase: RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao

    companion object {
        const val DATABASE_NAME = "feed_five_db"
    }
}