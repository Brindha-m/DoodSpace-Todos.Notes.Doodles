package com.implementing.cozyspace.domain.usecase.tasks

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.implementing.cozyspace.domain.repository.task.TaskRepository
import com.implementing.cozyspace.inappscreens.glance_widgets.TasksWidgetReceiver
import com.implementing.cozyspace.model.Task
import javax.inject.Inject

class AddTaskUseCase @Inject constructor(
    private val tasksRepository: TaskRepository,
) {
    suspend operator fun invoke(task: Task) : Long {
        return tasksRepository.insertTask(task)
    }
}
