package com.implementing.cozyspace.domain.usecase.tasks

import com.implementing.cozyspace.domain.repository.task.TaskRepository
import com.implementing.cozyspace.model.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchTasksUseCase @Inject constructor(
    private val tasksRepository: TaskRepository
) {
    operator fun invoke(query: String): Flow<List<Task>> {
        return tasksRepository.searchTasks(query)
    }
}