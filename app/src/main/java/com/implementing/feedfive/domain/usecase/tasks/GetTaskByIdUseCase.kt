package com.implementing.feedfive.domain.usecase.tasks

import com.implementing.feedfive.domain.repository.task.TaskRepository
import javax.inject.Inject

class GetTaskByIdUseCase @Inject constructor(
    private val tasksRepository: TaskRepository
) {
    suspend operator fun invoke(id: Int) = tasksRepository.getTaskById(id)
}