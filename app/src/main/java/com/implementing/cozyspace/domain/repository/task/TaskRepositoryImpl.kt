package com.implementing.cozyspace.domain.repository.task

import com.implementing.cozyspace.data.local.dao.TaskDao
import com.implementing.cozyspace.model.Task
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class TaskRepositoryImpl(
    private val taskDao: TaskDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): TaskRepository {
    override fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks()
    }

    override suspend fun getTaskById(id: Int): Task {
        return withContext(ioDispatcher) {
            taskDao.getTask(id)
        }
    }

    override fun searchTasks(title: String): Flow<List<Task>> {
        return taskDao.getTasksByTitle(title)
    }

    override suspend fun insertTask(task: Task): Long {
        return withContext(ioDispatcher) {
            taskDao.insertTask(task)
        }
    }

    override suspend fun updateTask(task: Task) {
        return withContext(ioDispatcher) {
            taskDao.updateTask(task)
        }
    }

    override suspend fun completeTask(id: Int, completed: Boolean) {
        return withContext(ioDispatcher) {
            taskDao.updateComplete(id, completed)
        }
    }

    override suspend fun deleteTask(task: Task) {
        return withContext(ioDispatcher) {
            taskDao.deleteTask(task)
        }
    }
}