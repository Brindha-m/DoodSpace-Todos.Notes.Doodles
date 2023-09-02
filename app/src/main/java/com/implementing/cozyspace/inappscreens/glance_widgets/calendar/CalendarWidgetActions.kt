package com.implementing.cozyspace.inappscreens.glance_widgets.calendar


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.core.net.toUri
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import com.implementing.cozyspace.mainscreens.MainActivity
import com.implementing.cozyspace.util.Constants

class AddEventAction : ActionCallback {
    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            "${Constants.CALENDAR_DETAILS_SCREEN_URI}/ ".toUri(),
            context,
            MainActivity::class.java
        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}

class NavigateToCalendarAction : ActionCallback {
    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Constants.CALENDAR_SCREEN_URI.toUri(),
            context,
            MainActivity::class.java
        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}

class CalendarWidgetItemClick : ActionCallback {
    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        parameters[eventJson]?.let {
            val intent = Intent(
                Intent.ACTION_VIEW,
                "${Constants.CALENDAR_DETAILS_SCREEN_URI}/$it".toUri(),
                context,
                MainActivity::class.java
            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }
}

class GoToSettingsAction : ActionCallback {
    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.data = Uri.fromParts("package", context.packageName, null)
        context.startActivity(intent)
    }
}

class RefreshCalendarAction : ActionCallback {
    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        val updateIntent = Intent(context, RefreshCalendarWidgetReceiver::class.java)
        context.sendBroadcast(updateIntent)
    }
}


val eventJson = ActionParameters.Key<String>("eventJson")