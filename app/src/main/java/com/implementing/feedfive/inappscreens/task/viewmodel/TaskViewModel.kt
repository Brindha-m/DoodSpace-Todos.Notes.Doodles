package com.implementing.feedfive.inappscreens.task.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.implementing.feedfive.R
import com.implementing.feedfive.domain.usecase.alarm.AddAlarmUseCase
import com.implementing.feedfive.domain.usecase.alarm.DeleteAlarmUseCase
import com.implementing.feedfive.domain.usecase.settings.GetSettingsUseCase
import com.implementing.feedfive.domain.usecase.settings.SaveSettingsUseCase
import com.implementing.feedfive.domain.usecase.tasks.AddTaskUseCase
import com.implementing.feedfive.domain.usecase.tasks.DeleteTaskUseCase
import com.implementing.feedfive.domain.usecase.tasks.GetAllTasksUseCase
import com.implementing.feedfive.domain.usecase.tasks.GetTaskByIdUseCase
import com.implementing.feedfive.domain.usecase.tasks.SearchTasksUseCase
import com.implementing.feedfive.domain.usecase.tasks.UpdateTaskCompletedUseCase
import com.implementing.feedfive.domain.usecase.tasks.UpdateTaskUseCase
import com.implementing.feedfive.getString
import com.implementing.feedfive.inappscreens.task.TaskEvent
import com.implementing.feedfive.model.Alarm
import com.implementing.feedfive.model.Task
import com.implementing.feedfive.util.Constants
import com.implementing.feedfive.util.Order
import com.implementing.feedfive.util.OrderType
import com.implementing.feedfive.util.toInt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val addTask: AddTaskUseCase,
    private val getAllTasks: GetAllTasksUseCase,
    private val getTaskUseCase: GetTaskByIdUseCase,
    private val updateTask: UpdateTaskUseCase,
    private val completeTask: UpdateTaskCompletedUseCase,
    getSettings: GetSettingsUseCase,
    private val saveSettings: SaveSettingsUseCase,
    private val addAlarm: AddAlarmUseCase,
    private val deleteAlarm: DeleteAlarmUseCase,
    private val deleteTask: DeleteTaskUseCase,
    private val searchTasksUseCase: SearchTasksUseCase
): ViewModel() {

    var tasksUiState by mutableStateOf(UiState())
        private set
    var taskDetailsUiState by mutableStateOf(TaskUiState())
        private set

    private var getTasksJob: Job? = null
    private var searchTasksJob: Job? = null

    fun onEvent(event: TaskEvent) {
        when (event) {
            is TaskEvent.AddTask -> {
                if (event.task.title.isNotBlank()) {
                    viewModelScope.launch {
                        val taskId = addTask(event.task)
                        if (event.task.dueDate != 0L)
                            addAlarm(
                                Alarm(
                                    taskId.toInt(),
                                    event.task.dueDate,
                                )
                            )
                    }

                } else
                    tasksUiState = tasksUiState.copy(error = getString(R.string.error_empty_title))
            }

            is TaskEvent.CompleteTask -> viewModelScope.launch {
                completeTask(event.task.id, event.complete)
                if (event.complete)
                    deleteAlarm(event.task.id)
            }
            TaskEvent.ErrorDisplayed -> {
                tasksUiState = tasksUiState.copy(error = null)
                taskDetailsUiState = taskDetailsUiState.copy(error = null)
            }

            is TaskEvent.UpdateOrder -> viewModelScope.launch {
                saveSettings(
                    intPreferencesKey(Constants.TASKS_ORDER_KEY),
                    event.order.toInt()
                )
            }

            is TaskEvent.ShowCompletedTasks -> viewModelScope.launch {
                saveSettings(
                    booleanPreferencesKey(Constants.SHOW_COMPLETED_TASKS_KEY),
                    event.showCompleted
                )
            }

            is TaskEvent.SearchTasks -> {
                viewModelScope.launch {
                    searchTasks(event.query)
                }
            }

            is TaskEvent.UpdateTask -> viewModelScope.launch {
                if (event.task.title.isBlank())
                    taskDetailsUiState = taskDetailsUiState.copy(error = getString(R.string.error_empty_title))
                else {
                    updateTask(event.task.copy(updatedDate = System.currentTimeMillis()))
                    if (event.task.dueDate != taskDetailsUiState.task.dueDate){
                        if (event.task.dueDate != 0L)
                            addAlarm(
                                Alarm(
                                    event.task.id,
                                    event.task.dueDate
                                )
                            )
                        else
                            deleteAlarm(event.task.id)
                    }
                    taskDetailsUiState = taskDetailsUiState.copy(navigateUp = true)
                }
            }

            is TaskEvent.DeleteTask -> viewModelScope.launch {
                deleteTask(event.task)
                if (event.task.dueDate != 0L)
                    deleteAlarm(event.task.id)
                taskDetailsUiState = taskDetailsUiState.copy(navigateUp = true)
            }

            is TaskEvent.GetTask -> viewModelScope.launch {
                taskDetailsUiState = taskDetailsUiState.copy(
                    task = getTaskUseCase(event.taskId)
                )
            }
        }
    }


    data class UiState(
        val tasks: List<Task> = emptyList(),
        val taskOrder: Order = Order.DateModified(OrderType.ASC()),
        val showCompletedTasks: Boolean = false,
        val error: String? = null,
        val searchTasks: List<Task> = emptyList()
    )

    data class TaskUiState(
        val task: Task = Task(""),
        val navigateUp: Boolean = false,
        val error: String? = null
    )

    private fun getTasks(order: Order, showCompleted: Boolean) {
        getTasksJob?.cancel()
        getTasksJob = getAllTasks(order)
            .map { list ->
                if(showCompleted)
                    list
                else
                    list.filter { !it.isCompleted }
            }.onEach {tasks ->
            tasksUiState = tasksUiState.copy(
                tasks = tasks,
                taskOrder = order,
                showCompletedTasks = showCompleted
            )
        }.launchIn(viewModelScope)
    }


    private fun searchTasks(query: String) {
        searchTasksJob?.cancel()
        searchTasksJob = searchTasksUseCase(query).onEach { tasks ->
            tasksUiState = tasksUiState.copy(
                searchTasks = tasks
            )
        }.launchIn(viewModelScope)
    }
}