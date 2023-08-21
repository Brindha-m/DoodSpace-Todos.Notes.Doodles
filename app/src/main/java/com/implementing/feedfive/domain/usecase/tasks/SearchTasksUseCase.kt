package com.implementing.feedfive.domain.usecase.tasks

import com.implementing.feedfive.domain.repository.task.TaskRepository
import com.implementing.feedfive.model.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchTasksUseCase @Inject constructor(
    private val tasksRepository: TaskRepository
) {
    operator fun invoke(query: String): Flow<List<Task>> {
        return tasksRepository.searchTasks(query)
    }
}