package com.implementing.feedfive.domain.usecase.tasks

import android.content.Context
import com.implementing.feedfive.domain.repository.task.TaskRepository
import javax.inject.Inject

class UpdateTaskCompletedUseCase @Inject constructor(
    private val tasksRepository: TaskRepository,
    private val context: Context
) {
    suspend operator fun invoke(taskId: Int, completed: Boolean) {
        tasksRepository.completeTask(taskId, completed)
        context.refreshTasksWidget()
    }
}