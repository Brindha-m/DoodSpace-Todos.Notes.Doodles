package com.implementing.cozyspace.domain.usecase.tasks

import android.content.Context
import com.implementing.cozyspace.domain.repository.task.TaskRepository
import com.implementing.cozyspace.domain.usecase.alarm.DeleteAlarmUseCase
import javax.inject.Inject

class UpdateTaskCompletedUseCase @Inject constructor(
    private val tasksRepository: TaskRepository,
    private val deleteAlarm: DeleteAlarmUseCase,
) {
    suspend operator fun invoke(taskId: Int, completed: Boolean) {
        tasksRepository.completeTask(taskId, completed)
        if (completed) {
            deleteAlarm(taskId)
        }
    }
}