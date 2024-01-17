package com.implementing.cozyspace.util.alarmUtils

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.implementing.cozyspace.domain.usecase.alarm.AddAlarmUseCase
import com.implementing.cozyspace.domain.usecase.alarm.DeleteAlarmUseCase
import com.implementing.cozyspace.domain.usecase.tasks.GetTaskByIdUseCase
import com.implementing.cozyspace.domain.usecase.tasks.UpdateTaskUseCase
import com.implementing.cozyspace.model.Alarm
import com.implementing.cozyspace.util.Constants
import com.implementing.cozyspace.util.TaskFrequency
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var deleteAlarmUseCase: DeleteAlarmUseCase

    @Inject
    lateinit var addAlarmUseCase: AddAlarmUseCase

    @Inject
    lateinit var getTaskByIdUseCase: GetTaskByIdUseCase

    @Inject
    lateinit var updateTaskUseCase: UpdateTaskUseCase

    private val scope = CoroutineScope(Dispatchers.Default)

    override fun onReceive(context: Context?, intent: Intent?) {
        val pendingResult = goAsync()
        scope.launch {

            val task =
                intent?.getIntExtra(Constants.TASK_ID_EXTRA, 0)?.let { getTaskByIdUseCase(it) }
                    ?: kotlin.run {
                        pendingResult.finish()
                        return@launch
                    }
            val notificationJob = launch {
                val manager =
                    context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                manager.sendNotification(task, context, task.id)
                if (!task.recurring) deleteAlarmUseCase(task.id)
            }
            val recurrenceJob = launch {
                if (task.recurring) {
                    val calendar = Calendar.getInstance().apply { timeInMillis = task.dueDate }
                    when (task.frequency) {
                        TaskFrequency.DAILY.value -> calendar.add(Calendar.DAY_OF_YEAR, task.frequencyAmount)
                        TaskFrequency.HOURLY.value -> calendar.add(Calendar.HOUR, task.frequencyAmount)
                        TaskFrequency.EVERY_MINUTES.value -> calendar.add(Calendar.MINUTE, task.frequencyAmount)
                        TaskFrequency.WEEKLY.value -> calendar.add(Calendar.WEEK_OF_YEAR, task.frequencyAmount)
                        TaskFrequency.MONTHLY.value -> calendar.add(Calendar.MONTH, task.frequencyAmount)
                        TaskFrequency.ANNUAL.value -> calendar.add(Calendar.YEAR, task.frequencyAmount)
                        else -> calendar.add(Calendar.DAY_OF_YEAR, task.frequencyAmount)
                    }
                    val newTask = task.copy(
                        dueDate = calendar.timeInMillis,
                    )
                    updateTaskUseCase(newTask)
                    addAlarmUseCase(Alarm(newTask.id, newTask.dueDate))
                }
            }
            notificationJob.join()
            recurrenceJob.join()
            pendingResult.finish()
        }
    }
}


/*
private fun isScheduleExactAlarmPermissionGranted(context: Context?): Boolean {
        val permission = "android.permission.SCHEDULE_EXACT_ALARM"
        val permissionStatus = context?.let { ActivityCompat.checkSelfPermission(it, permission) }
        return permissionStatus == PackageManager.PERMISSION_GRANTED
    }
 */