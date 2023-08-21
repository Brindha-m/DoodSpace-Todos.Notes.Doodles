package com.implementing.feedfive.util.alarmUtils


import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.implementing.feedfive.domain.usecase.tasks.UpdateTaskCompletedUseCase
import com.implementing.feedfive.domain.usecase.tasks.refreshTasksWidget
import com.implementing.feedfive.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class TaskActionButtonBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var updateTaskCompleted: UpdateTaskCompletedUseCase

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Constants.ACTION_COMPLETE) {
            runBlocking {
                val taskId = intent.getIntExtra(Constants.TASK_ID_EXTRA, 0)
                updateTaskCompleted(taskId, true)
                val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                manager.cancel(taskId)
                context.refreshTasksWidget()
            }
        }
    }
}