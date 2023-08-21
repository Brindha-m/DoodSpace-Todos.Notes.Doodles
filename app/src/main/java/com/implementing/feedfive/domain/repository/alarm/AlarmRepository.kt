package com.implementing.feedfive.domain.repository.alarm

import com.implementing.feedfive.model.Alarm

interface AlarmRepository {
    suspend fun getAlarms(): List<Alarm>

    suspend fun insertAlarm(alarm: Alarm)

    suspend fun deleteAlarm(alarm: Alarm)

    suspend fun deleteAlarm(id: Int)
}