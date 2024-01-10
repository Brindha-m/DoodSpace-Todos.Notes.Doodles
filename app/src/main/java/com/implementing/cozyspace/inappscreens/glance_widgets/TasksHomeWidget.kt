package com.implementing.cozyspace.inappscreens.glance_widgets

import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import com.implementing.cozyspace.di.TasksWidgetEntryPoint
import com.implementing.cozyspace.inappscreens.task.screens.widgets.TasksHomeScreenWidget
import com.implementing.cozyspace.util.Constants
import com.implementing.cozyspace.util.Order
import com.implementing.cozyspace.util.OrderType
import com.implementing.cozyspace.util.toInt
import com.implementing.cozyspace.util.toOrder
import dagger.hilt.EntryPoints
import dagger.hilt.android.AndroidEntryPoint

class TasksHomeWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val entryPoint = EntryPoints.get(context, TasksWidgetEntryPoint::class.java)
        provideContent {
            val order by entryPoint.getSettingsUseCase().invoke(
                intPreferencesKey(Constants.TASKS_ORDER_KEY),
                Order.DateModified(OrderType.ASC()).toInt()
            ).collectAsState(Order.DateModified(OrderType.ASC()).toInt())
            val showCompletedTasks by entryPoint.getSettingsUseCase().invoke(
                booleanPreferencesKey(Constants.SHOW_COMPLETED_TASKS_KEY),
                false
            ).collectAsState(false)
            val tasks by entryPoint.getAllTasksUseCase().invoke(order.toOrder(), showCompletedTasks)
                .collectAsState(emptyList())
            TasksHomeScreenWidget(
                tasks
            )
        }
    }
}

@AndroidEntryPoint
class TasksWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = TasksHomeWidget()
}