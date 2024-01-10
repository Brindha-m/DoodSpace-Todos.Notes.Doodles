package com.implementing.cozyspace.domain.usecase.tasks

import android.content.Context
import com.implementing.cozyspace.domain.repository.task.TaskRepository
import com.implementing.cozyspace.model.Task
import javax.inject.Inject

class UpdateTaskUseCase @Inject constructor(
    private val tasksRepository: TaskRepository,
    private val context: Context
) {
    suspend operator fun invoke(task: Task) {
        tasksRepository.updateTask(task)
    }
}