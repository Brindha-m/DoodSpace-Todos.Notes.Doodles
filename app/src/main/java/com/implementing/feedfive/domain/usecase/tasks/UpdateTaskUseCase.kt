package com.implementing.feedfive.domain.usecase.tasks

import android.content.Context
import com.implementing.feedfive.domain.repository.task.TaskRepository
import com.implementing.feedfive.model.Task
import javax.inject.Inject

class UpdateTaskUseCase @Inject constructor(
    private val tasksRepository: TaskRepository,
    private val context: Context
) {
    suspend operator fun invoke(task: Task) {
        tasksRepository.updateTask(task)
        context.refreshTasksWidget()
    }
}