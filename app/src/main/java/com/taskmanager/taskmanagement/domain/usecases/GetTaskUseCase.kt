package com.taskmanager.taskmanagement.domain.usecases

import com.taskmanager.taskmanagement.data.local.CacheResult
import com.taskmanager.taskmanagement.data.util.Resource
import com.taskmanager.taskmanagement.domain.model.Task
import com.taskmanager.taskmanagement.domain.repository.TasksRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTaskUseCase @Inject constructor(
    private val repository: TasksRepository
) {
    operator fun invoke(taskId: String): Flow<Resource<Task>?> {
        return repository.getTask(taskId)
    }
}