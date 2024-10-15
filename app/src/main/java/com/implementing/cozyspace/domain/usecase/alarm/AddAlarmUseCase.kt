package com.implementing.cozyspace.domain.usecase.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import com.implementing.cozyspace.domain.repository.alarm.AlarmRepository
import com.implementing.cozyspace.model.Alarm
import com.implementing.cozyspace.util.alarmUtils.scheduleAlarm
import javax.inject.Inject

class AddAlarmUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository,
    private val context: Context
) {
    suspend operator fun invoke(alarm: Alarm) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.scheduleAlarm(alarm, context)
        alarmRepository.insertAlarm(alarm)
    }
}