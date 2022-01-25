package com.taskmanager.taskmanagement.domain.usecases

import com.taskmanager.taskmanagement.domain.repository.TaskListRepository

class DeleteTaskListUseCase(
    private val repository: TaskListRepository
) {
    suspend operator fun invoke(id: String){
        repository.deleteTaskList(id)
    }
}