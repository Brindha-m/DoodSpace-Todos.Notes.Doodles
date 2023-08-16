package com.implementing.feedfive.domain.repository.diary

import com.implementing.feedfive.data.local.dao.DiaryDao
import com.implementing.feedfive.model.Diary
import kotlinx.coroutines.flow.Flow

interface DiaryRepository{
    fun getAllEntries(): Flow<List<Diary>>

    suspend fun getEntry(id: Int): Diary

    suspend fun searchEntries(title: String): List<Diary>

    suspend fun addEntry(diary: Diary)

    suspend fun updateEntry(diary: Diary)

    suspend fun deleteEntry(diary: Diary)

}