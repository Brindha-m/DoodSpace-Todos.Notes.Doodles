package com.implementing.cozyspace.domain.repository.alarm

import com.implementing.cozyspace.model.Alarm

interface AlarmRepository {
    suspend fun getAlarms(): List<Alarm>

    suspend fun insertAlarm(alarm: Alarm)

    suspend fun deleteAlarm(alarm: Alarm)

    suspend fun deleteAlarm(id: Int)
}