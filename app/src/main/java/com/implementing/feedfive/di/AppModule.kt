package com.implementing.feedfive.di

import android.content.Context
import androidx.room.Room
import com.implementing.feedfive.data.local.FeedFiveDatabase
import com.implementing.feedfive.data.local.dao.AlarmDao
import com.implementing.feedfive.data.local.dao.BookmarkDao
import com.implementing.feedfive.data.local.dao.DiaryDao
import com.implementing.feedfive.data.local.dao.NoteDao
import com.implementing.feedfive.data.local.dao.TaskDao
import com.implementing.feedfive.data.local.room.migration.MIGRATION_5_6
import com.implementing.feedfive.dataStore
import com.implementing.feedfive.domain.repository.alarm.AlarmRepository
import com.implementing.feedfive.domain.repository.alarm.AlarmRepositoryImpl
import com.implementing.feedfive.domain.repository.bookmark.BookmarkRepository
import com.implementing.feedfive.domain.repository.bookmark.BookmarkRepositoryImpl
import com.implementing.feedfive.domain.repository.diary.DiaryRepository
import com.implementing.feedfive.domain.repository.diary.DiaryRepositoryImpl
import com.implementing.feedfive.domain.repository.note.NoteRepository
import com.implementing.feedfive.domain.repository.note.NoteRepositoryImpl
import com.implementing.feedfive.domain.repository.settings.SettingsRepository
import com.implementing.feedfive.domain.repository.settings.SettingsRepositoryImpl
import com.implementing.feedfive.domain.repository.task.TaskRepository
import com.implementing.feedfive.domain.repository.task.TaskRepositoryImpl
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
            .addMigrations(MIGRATION_5_6)
            .build()

// Bookmark repo and dao
    @Singleton
    @Provides
    fun provideBookmarkDao(feedFiveDatabase: FeedFiveDatabase) = feedFiveDatabase.bookmarkDao()

    @Singleton
    @Provides
    fun provideBookmarkRepository(bookmarkDao: BookmarkDao): BookmarkRepository = BookmarkRepositoryImpl(bookmarkDao)

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


// Setting Repo

    @Singleton
    @Provides
    fun provideSettingsRepository(@ApplicationContext context: Context): SettingsRepository = SettingsRepositoryImpl(context.dataStore)

    @Singleton
    @Provides
    fun provideAppContext(@ApplicationContext context: Context) = context
}
