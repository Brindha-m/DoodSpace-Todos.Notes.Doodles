package com.implementing.cozyspace.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.implementing.cozyspace.data.local.dao.AlarmDao
import com.implementing.cozyspace.data.local.dao.BookmarkDao
import com.implementing.cozyspace.data.local.dao.DiaryDao
import com.implementing.cozyspace.data.local.dao.NoteDao
import com.implementing.cozyspace.data.local.dao.TaskDao
import com.implementing.cozyspace.data.local.room.converters.DBConverters
import com.implementing.cozyspace.model.Alarm
import com.implementing.cozyspace.model.Bookmark
import com.implementing.cozyspace.model.Diary
import com.implementing.cozyspace.model.Note
import com.implementing.cozyspace.model.NoteFolder
import com.implementing.cozyspace.model.Task

@Database(
    entities = [Bookmark::class, Diary::class, Note::class, NoteFolder::class, Task::class, Alarm::class],
    version = 10, exportSchema = false)
//@AutoMigration(from = 1, to = 2)
@TypeConverters(DBConverters::class)
abstract class FeedFiveDatabase: RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun diaryDao(): DiaryDao
    abstract fun noteDao(): NoteDao
    abstract fun taskDao(): TaskDao
    abstract fun alarmDao(): AlarmDao

    companion object {
        const val DATABASE_NAME = "feed_five_db"
    }
}