package com.implementing.cozyspace.util.alarmUtils


import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.implementing.cozyspace.domain.usecase.alarm.GetAllAlarmsUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class BootBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var getAllAlarms: GetAllAlarmsUseCase

//    @RequiresApi(Build.VERSION_CODES.S)
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            runBlocking {
                val alarms = getAllAlarms()
                alarms.forEach {
                    alarmManager.scheduleAlarm(it, context)
                }
            }
            // Check for SCHEDULE_EXACT_ALARM permission
//            if (alarmManager.canScheduleExactAlarms()) {
//                runBlocking {
//                    val alarms = getAllAlarms()
//                    alarms.forEach {
//                        alarmManager.scheduleAlarm(it, context)
//                    }
//                }
//            } else {
//                // Handle the case where the permission is not granted
//                // You may show a notification, log a message, or take other appropriate actions
//            }
        }
    }
}