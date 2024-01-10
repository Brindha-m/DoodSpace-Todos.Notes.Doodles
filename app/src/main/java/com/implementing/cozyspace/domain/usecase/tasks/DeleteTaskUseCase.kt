package com.implementing.cozyspace.domain.usecase.tasks


import android.content.Context
import com.implementing.cozyspace.domain.repository.task.TaskRepository
import com.implementing.cozyspace.model.Task
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
) {
    suspend operator fun invoke(task: Task) {
        taskRepository.deleteTask(task)
    }
}