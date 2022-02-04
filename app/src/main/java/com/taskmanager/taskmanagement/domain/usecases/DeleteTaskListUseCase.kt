package com.taskmanager.taskmanagement.domain.usecases

import com.taskmanager.taskmanagement.domain.repository.TaskListRepository

class DeleteTaskListUseCase(
    private val repository: TaskListRepository
) {
    suspend operator fun invoke(taskListId: String, projectId: String){
        repository.deleteTaskList(taskListId, projectId)
    }
}