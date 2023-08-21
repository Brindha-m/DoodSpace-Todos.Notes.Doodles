package com.implementing.feedfive.domain.repository.alarm

import com.implementing.feedfive.data.local.dao.AlarmDao
import com.implementing.feedfive.model.Alarm
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AlarmRepositoryImpl(
    private val alarmDao: AlarmDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): AlarmRepository {

    override suspend fun getAlarms(): List<Alarm> {
        return withContext(ioDispatcher) {
            alarmDao.getAll()
        }
    }

    override suspend fun insertAlarm(alarm: Alarm) {
        withContext(ioDispatcher) {
            alarmDao.insert(alarm)
        }
    }

    override suspend fun deleteAlarm(alarm: Alarm) {
        withContext(ioDispatcher) {
            alarmDao.delete(alarm)
        }
    }

    override suspend fun deleteAlarm(id: Int) {
        withContext(ioDispatcher) {
            alarmDao.delete(id)
        }
    }
}