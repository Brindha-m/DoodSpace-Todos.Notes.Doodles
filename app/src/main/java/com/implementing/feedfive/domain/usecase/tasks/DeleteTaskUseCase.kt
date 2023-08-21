package com.implementing.feedfive.domain.usecase.tasks


import android.content.Context
import com.implementing.feedfive.domain.repository.task.TaskRepository
import com.implementing.feedfive.model.Task
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
    private val context: Context
) {
    suspend operator fun invoke(task: Task) {
        taskRepository.deleteTask(task)
        context.refreshTasksWidget()
    }
}