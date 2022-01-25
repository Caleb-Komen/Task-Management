package com.taskmanager.taskmanagement.domain.usecases

import com.taskmanager.taskmanagement.domain.model.Task
import com.taskmanager.taskmanagement.domain.repository.TasksRepository

class InsertTaskUseCase(
    private val repository: TasksRepository
) {
    suspend operator fun invoke(task: Task){
        repository.createTask(task)
    }
}