package com.implementing.feedfive.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.implementing.feedfive.data.local.dao.BookmarkDao
import com.implementing.feedfive.data.local.dao.DiaryDao
import com.implementing.feedfive.data.local.room.converters.DBConverters
import com.implementing.feedfive.model.Bookmark
import com.implementing.feedfive.model.Diary

@Database(
    entities = [Bookmark::class, Diary::class],
    version = 3 )
//@AutoMigration(from = 1, to = 2)
@TypeConverters(DBConverters::class)
abstract class FeedFiveDatabase: RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun diaryDao() : DiaryDao

    companion object {
        const val DATABASE_NAME = "feed_five_db"
    }
}