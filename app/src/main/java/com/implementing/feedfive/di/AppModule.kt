package com.implementing.feedfive.di

import android.content.Context
import androidx.room.Room
import androidx.work.impl.Migration_1_2
import com.implementing.feedfive.data.local.FeedFiveDatabase
import com.implementing.feedfive.data.local.dao.BookmarkDao
import com.implementing.feedfive.domain.repository.bookmark.BookmarkRepository
import com.implementing.feedfive.domain.repository.bookmark.BookmarkRepositoryImpl
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
            .addMigrations(Migration_1_2)
            .build()

    @Singleton
    @Provides
    fun provideBookmarkDao(feedFiveDatabase: FeedFiveDatabase) = feedFiveDatabase.bookmarkDao()

    @Singleton
    @Provides
    fun provideBookmarkRepository(bookmarkDao: BookmarkDao): BookmarkRepository = BookmarkRepositoryImpl(bookmarkDao)
}