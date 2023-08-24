package com.implementing.feedfive.mainscreens.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.implementing.feedfive.domain.usecase.calendar.GetAllCalendarEventsUseCase
import com.implementing.feedfive.domain.usecase.diary.GetAllDiaryEntryUseCase
import com.implementing.feedfive.domain.usecase.settings.GetSettingsUseCase
import com.implementing.feedfive.domain.usecase.tasks.GetAllTasksUseCase
import com.implementing.feedfive.domain.usecase.tasks.UpdateTaskUseCase
import com.implementing.feedfive.model.CalendarEvent
import com.implementing.feedfive.model.Diary
import com.implementing.feedfive.model.Task
import com.implementing.feedfive.ui.theme.Jost
import com.implementing.feedfive.util.Constants
import com.implementing.feedfive.util.Order
import com.implementing.feedfive.util.OrderType
import com.implementing.feedfive.util.StartUpScreenSettings
import com.implementing.feedfive.util.ThemeSettings
import com.implementing.feedfive.util.inTheLastWeek
import com.implementing.feedfive.util.toInt
import com.implementing.feedfive.util.toIntList
import com.implementing.feedfive.util.toOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MainViewModel @Inject constructor(
    private val getSettings: GetSettingsUseCase,
    private val getAllTasks: GetAllTasksUseCase,
    private val getAllEntriesUseCase: GetAllDiaryEntryUseCase,
    private val updateTask: UpdateTaskUseCase,
    private val getAllCalendarEventsUseCase: GetAllCalendarEventsUseCase
): ViewModel() {

    var uiState by mutableStateOf(UiState())
        private set

    private var refreshTasksJob : Job? = null

    val themeMode = getSettings(intPreferencesKey(Constants.SETTINGS_THEME_KEY), ThemeSettings.AUTO.value)
    val defaultStartUpScreen = getSettings(intPreferencesKey(Constants.DEFAULT_START_UP_SCREEN_KEY), StartUpScreenSettings.SPACES.value)
    val font = getSettings(intPreferencesKey(Constants.APP_FONT_KEY), Jost.toInt())
    val blockScreenshots = getSettings(booleanPreferencesKey(Constants.BLOCK_SCREENSHOTS_KEY), false)

    fun onDashboardEvent(event: DashboardEvent) {
        when(event) {
            is DashboardEvent.ReadPermissionChanged -> {
                if (event.hasPermission)
                    getCalendarEvents()
            }
            is DashboardEvent.UpdateTask -> viewModelScope.launch {
                updateTask(event.task)
            }
            DashboardEvent.InitAll -> collectDashboardData()
        }
    }

    data class UiState(
        val dashBoardTasks: List<Task> = emptyList(),
        val dashBoardEvents: Map<String, List<CalendarEvent>> = emptyMap(),
        val summaryTasks: List<Task> = emptyList(),
        val dashBoardEntries: List<Diary> = emptyList()
    )


    private fun getCalendarEvents() = viewModelScope.launch {
        val excluded = getSettings(
            stringSetPreferencesKey(Constants.EXCLUDED_CALENDARS_KEY),
            emptySet()
        ).first()

        val events = getAllCalendarEventsUseCase(excluded.toIntList())
        uiState = uiState.copy(
            dashBoardEvents = events
        )
    }

    private fun collectDashboardData() = viewModelScope.launch {
        combine(
            getSettings(
                intPreferencesKey(Constants.TASKS_ORDER_KEY),
                Order.DateModified(OrderType.ASC()).toInt()
            ),
            getSettings(
                booleanPreferencesKey(Constants.SHOW_COMPLETED_TASKS_KEY),
                false
            ),
            getAllEntriesUseCase(Order.DateCreated(OrderType.ASC()))
        ) { order, showCompleted, entries ->
            uiState = uiState.copy(
                dashBoardEntries = entries,
            )
            refreshTasks(order.toOrder(), showCompleted)
        }.collect()
    }


    private fun refreshTasks(order: Order, showCompleted: Boolean) {
        refreshTasksJob?.cancel()
        refreshTasksJob = getAllTasks(order).onEach { tasks ->
            uiState = uiState.copy(
                dashBoardTasks = if (showCompleted) tasks else tasks.filter { !it.isCompleted },
                summaryTasks = tasks.filter { it.createdDate.inTheLastWeek() }
            )
        }.launchIn(viewModelScope)
    }

}