package com.implementing.cozyspace.domain.usecase.alarm


import android.app.AlarmManager
import android.content.Context
import com.implementing.cozyspace.domain.repository.alarm.AlarmRepository
import com.implementing.cozyspace.util.alarmUtils.cancelAlarm
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