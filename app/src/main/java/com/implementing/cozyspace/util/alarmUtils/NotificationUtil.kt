package com.implementing.cozyspace.util.alarmUtils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import com.implementing.cozyspace.R
import com.implementing.cozyspace.getString
import com.implementing.cozyspace.mainscreens.MainActivity
import com.implementing.cozyspace.model.Task
import com.implementing.cozyspace.util.Constants
import com.implementing.cozyspace.util.Priority
import com.implementing.cozyspace.util.toInt


fun NotificationManager.sendNotification(task: Task, context: Context, id: Int) {
    val completeIntent = Intent(context, TaskActionButtonBroadcastReceiver::class.java).apply {
        action = Constants.ACTION_COMPLETE
        putExtra(Constants.TASK_ID_EXTRA, task.id)
    }
    val completePendingIntent: PendingIntent =
        PendingIntent.getBroadcast(
            context,
            task.id,
            completeIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

    val taskDetailIntent = Intent(
        Intent.ACTION_VIEW,
        "${Constants.TASK_DETAILS_URI}/${task.id}".toUri(),
        context,
        MainActivity::class.java
    )
    val taskDetailsPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
        addNextIntentWithParentStack(taskDetailIntent)
        getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
    }

//    val largeIconBitmap = BitmapFactory.decodeResource(reso, R.drawable.dood_space)
    val largeIconBitmap = ContextCompat.getDrawable(context, R.drawable.dood_nn_bmp)?.toBitmap()


    val notification = NotificationCompat.Builder(context, Constants.REMINDERS_CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_stat_dood_space)
        .setLargeIcon(largeIconBitmap)
        .setContentTitle(task.title)
        .setContentText(task.description)
        .setContentIntent(taskDetailsPendingIntent)
        .setPriority(
            when (task.priority) {
                Priority.LOW.toInt() -> NotificationCompat.PRIORITY_DEFAULT
                Priority.MEDIUM.toInt() -> NotificationCompat.PRIORITY_HIGH
                Priority.HIGH.toInt() -> NotificationCompat.PRIORITY_MAX
                else -> NotificationCompat.PRIORITY_DEFAULT
            }
        )
        .addAction(R.drawable.ic_check, getString(R.string.complete), completePendingIntent)
        .setAutoCancel(true)
        .build()

    notify(id, notification)
}