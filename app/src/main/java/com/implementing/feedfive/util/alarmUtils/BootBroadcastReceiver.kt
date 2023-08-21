package com.implementing.feedfive.util.alarmUtils


import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.implementing.feedfive.domain.usecase.alarm.GetAllAlarmsUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class BootBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var getAllAlarms: GetAllAlarmsUseCase

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            runBlocking {
                val alarms = getAllAlarms()
                alarms.forEach {
                    alarmManager.scheduleAlarm(it, context)
                }
            }
        }

    }

}