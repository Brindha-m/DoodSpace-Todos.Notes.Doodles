package com.implementing.cozyspace.inappscreens.glance_widgets

import android.appwidget.AppWidgetManager
import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.currentState
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.implementing.cozyspace.domain.usecase.settings.GetSettingsUseCase
import com.implementing.cozyspace.domain.usecase.tasks.GetAllTasksUseCase
import com.implementing.cozyspace.inappscreens.task.screens.widgets.TasksHomeScreenWidget
import com.implementing.cozyspace.model.Task
import com.implementing.cozyspace.util.Constants
import com.implementing.cozyspace.util.Order
import com.implementing.cozyspace.util.OrderType
import com.implementing.cozyspace.util.toInt
import com.implementing.cozyspace.util.toOrder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class TasksHomeWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val gson = Gson()
            val type = object : TypeToken<List<Task>>() {}.type
            val prefs = currentState<Preferences>()
            val tasks: List<Task> = gson.fromJson(prefs[stringPreferencesKey("tasks")], type) ?: emptyList()
            TasksHomeScreenWidget(
                tasks
            )
        }
    }
}

@AndroidEntryPoint
class TasksWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = TasksHomeWidget()
    private val coroutineScope = MainScope()

    @Inject
    lateinit var getAllTasks: GetAllTasksUseCase

    @Inject
    lateinit var getSettings: GetSettingsUseCase

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        observeData(context)
    }

    private fun observeData(context: Context) {
        coroutineScope.launch {
            val gson = Gson()
            val type = object : TypeToken<List<Task>>() {}.type
            val order = getSettings(
                intPreferencesKey(Constants.TASKS_ORDER_KEY),
                Order.DateModified(OrderType.ASC()).toInt()
            ).first()
            val showCompletedTasks = getSettings(
                booleanPreferencesKey(Constants.SHOW_COMPLETED_TASKS_KEY),
                false
            ).first()
            val tasks = getAllTasks(order.toOrder())
                .map { list ->
                if (showCompletedTasks)
                    list
                else
                    list.filter { !it.isCompleted }
            }.first()
            val tasksJson = gson.toJson(tasks, type)
            val glanceId =
                GlanceAppWidgetManager(context).getGlanceIds(TasksHomeWidget::class.java)
                    .firstOrNull()

            glanceId?.let {
                updateAppWidgetState(context, PreferencesGlanceStateDefinition, it) { pref ->
                    pref.toMutablePreferences().apply {
                        this[stringPreferencesKey("tasks")] = tasksJson
                    }
                }
                glanceAppWidget.update(context, it)
            }
        }
    }

}