package com.implementing.feedfive.domain.usecase.alarm


import android.app.AlarmManager
import android.content.Context
import com.implementing.feedfive.domain.repository.alarm.AlarmRepository
import com.implementing.feedfive.util.alarmUtils.cancelAlarm
import javax.inject.Inject

class DeleteAlarmUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository,
    private val context: Context
) {
    suspend operator fun invoke(alarmId: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancelAlarm(alarmId, context)
        alarmRepository.deleteAlarm(alarmId)
    }
}