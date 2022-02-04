package com.taskmanager.taskmanagement.domain.usecases

import com.taskmanager.taskmanagement.domain.repository.TasksRepository

class DeleteAllTaskUseCase(
    private val repository: TasksRepository
) {
    suspend operator fun invoke(projectId: String){
        repository.deleteAllTasks(projectId)
    }
}