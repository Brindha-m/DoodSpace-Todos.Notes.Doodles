package com.implementing.cozyspace.inappscreens.task.service

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.service.quicksettings.TileService
import androidx.core.net.toUri
import com.implementing.cozyspace.mainscreens.MainActivity
import com.implementing.cozyspace.util.Constants


//class AddTaskTileService: TileService() {
//
//    @SuppressLint("StartActivityAndCollapseDeprecated")
//    override fun onClick() {
//        super.onClick()
//        val intent = Intent(
//            Intent.ACTION_VIEW,
//            "${Constants.TASKS_SCREEN_URI}/true".toUri(),
//            this,
//            MainActivity::class.java
//        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        startActivityAndCollapse(intent)
//    }
//}

@Suppress("DEPRECATION")
class AddTaskTileService: TileService() {

    @SuppressLint("StartActivityAndCollapseDeprecated")
    override fun onClick() {
        super.onClick()
        val intent = Intent(
            Intent.ACTION_VIEW,
            "${Constants.TASKS_SCREEN_URI}?${Constants.ADD_TASK_ARG}=true".toUri(),
        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
            startActivityAndCollapse(pendingIntent)
        } else {
            startActivityAndCollapse(intent)
        }

    }
}