package com.taskmanager.taskmanagement.domain.usecases

import com.taskmanager.taskmanagement.data.local.CacheResult
import com.taskmanager.taskmanagement.domain.model.Task
import com.taskmanager.taskmanagement.domain.repository.TasksRepository
import kotlinx.coroutines.flow.Flow

class GetTaskUseCase(
    private val repository: TasksRepository
) {
    operator fun invoke(taskId: String): Flow<CacheResult<Task>> {
        return repository.getTask(taskId)
    }
}