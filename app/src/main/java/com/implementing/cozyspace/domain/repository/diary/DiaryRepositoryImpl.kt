package com.implementing.cozyspace.domain.repository.diary

import com.implementing.cozyspace.data.local.dao.DiaryDao
import com.implementing.cozyspace.model.Diary
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class DiaryRepositoryImpl(
    private val diaryDao: DiaryDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): DiaryRepository {
    override fun getAllEntries(): Flow<List<Diary>> {
        return diaryDao.getAllEntries()
    }

    override suspend fun getEntry(id: Int): Diary {
        return withContext(ioDispatcher) {
            diaryDao.getEntry(id)
        }
    }

    override suspend fun searchEntries(title: String): List<Diary> {
        return withContext(ioDispatcher) {
            diaryDao.getEntriesByTitle(title)
        }
    }

    override suspend fun addEntry(diary: Diary) {
        withContext(ioDispatcher) {
            diaryDao.insertEntry(diary)
        }
    }

    override suspend fun updateEntry(diary: Diary) {
        withContext(ioDispatcher) {
            diaryDao.updateEntry(diary)
        }
    }

    override suspend fun deleteEntry(diary: Diary) {
        withContext(ioDispatcher) {
            diaryDao.deleteEntry(diary)
        }
    }

}
