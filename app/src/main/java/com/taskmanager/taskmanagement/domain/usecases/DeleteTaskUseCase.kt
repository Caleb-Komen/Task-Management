package com.taskmanager.taskmanagement.domain.usecases

import com.taskmanager.taskmanagement.domain.repository.TasksRepository

class DeleteTaskUseCase(
    private val repository: TasksRepository
) {
    suspend operator fun invoke(taskId: String){
        repository.deleteTask(taskId)
    }
}