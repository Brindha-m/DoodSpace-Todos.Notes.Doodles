package com.implementing.cozyspace.di

import android.content.Context
import androidx.room.Room
import com.implementing.cozyspace.data.local.FeedFiveDatabase
import com.implementing.cozyspace.data.local.dao.AlarmDao
import com.implementing.cozyspace.data.local.dao.BookmarkDao
import com.implementing.cozyspace.data.local.dao.DiaryDao
import com.implementing.cozyspace.data.local.dao.NoteDao
import com.implementing.cozyspace.data.local.dao.TaskDao
import com.implementing.cozyspace.data.local.room.migration.MIGRATION_5_6
import com.implementing.cozyspace.data.local.room.migration.MIGRATION_6_7
import com.implementing.cozyspace.dataStore
import com.implementing.cozyspace.domain.repository.alarm.AlarmRepository
import com.implementing.cozyspace.domain.repository.alarm.AlarmRepositoryImpl
import com.implementing.cozyspace.domain.repository.bookmark.BookmarkRepository
import com.implementing.cozyspace.domain.repository.bookmark.BookmarkRepositoryImpl
import com.implementing.cozyspace.domain.repository.calendar.CalendarRepository
import com.implementing.cozyspace.domain.repository.calendar.CalendarRepositoryImpl
import com.implementing.cozyspace.domain.repository.diary.DiaryRepository
import com.implementing.cozyspace.domain.repository.diary.DiaryRepositoryImpl
import com.implementing.cozyspace.domain.repository.note.NoteRepository
import com.implementing.cozyspace.domain.repository.note.NoteRepositoryImpl
import com.implementing.cozyspace.domain.repository.settings.SettingsRepository
import com.implementing.cozyspace.domain.repository.settings.SettingsRepositoryImpl
import com.implementing.cozyspace.domain.repository.task.TaskRepository
import com.implementing.cozyspace.domain.repository.task.TaskRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideLocalDataBase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            FeedFiveDatabase::class.java,
            FeedFiveDatabase.DATABASE_NAME
        )
//            .fallbackToDestructiveMigration()
            .addMigrations(MIGRATION_5_6, MIGRATION_6_7)
            .build()

    // Bookmark repo and dao
    @Singleton
    @Provides
    fun provideBookmarkDao(feedFiveDatabase: FeedFiveDatabase) = feedFiveDatabase.bookmarkDao()

    @Singleton
    @Provides
    fun provideBookmarkRepository(bookmarkDao: BookmarkDao): BookmarkRepository =
        BookmarkRepositoryImpl(bookmarkDao)

    // Diary repo and dao
    @Singleton
    @Provides
    fun provideDiaryDao(feedFiveDatabase: FeedFiveDatabase) = feedFiveDatabase.diaryDao()

    @Singleton
    @Provides
    fun provideDiaryRepository(diaryDao: DiaryDao): DiaryRepository = DiaryRepositoryImpl(diaryDao)

    // Note repo and dao
    @Singleton
    @Provides
    fun provideNoteDao(feedFiveDatabase: FeedFiveDatabase) = feedFiveDatabase.noteDao()

    @Singleton
    @Provides
    fun provideNoteRepository(noteDao: NoteDao): NoteRepository = NoteRepositoryImpl(noteDao)

// Tasks Repo and dao

    @Singleton
    @Provides
    fun provideTaskDao(feedFiveDatabase: FeedFiveDatabase) = feedFiveDatabase.taskDao()

    @Singleton
    @Provides
    fun provideTaskRepository(taskDao: TaskDao): TaskRepository = TaskRepositoryImpl(taskDao)

    // Alarms dao and repository
    @Singleton
    @Provides
    fun provideAlarmDao(feedFiveDatabase: FeedFiveDatabase) = feedFiveDatabase.alarmDao()

    @Singleton
    @Provides
    fun provideAlarmRepository(alarmDao: AlarmDao): AlarmRepository = AlarmRepositoryImpl(alarmDao)

    // Calendar Repo only
    @Singleton
    @Provides
    fun provideCalendarRepository(@ApplicationContext context: Context): CalendarRepository =
        CalendarRepositoryImpl(context)


// Setting Repo

    @Singleton
    @Provides
    fun provideSettingsRepository(@ApplicationContext context: Context): SettingsRepository =
        SettingsRepositoryImpl(context.dataStore)

    @Singleton
    @Provides
    fun provideAppContext(@ApplicationContext context: Context) = context
}
