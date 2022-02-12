package com.taskmanager.taskmanagement.domain.usecases

import com.taskmanager.taskmanagement.domain.repository.TasksRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    private val repository: TasksRepository
) {
    suspend operator fun invoke(taskId: String, projectId: String){
        repository.deleteTask(taskId, projectId)
    }
}