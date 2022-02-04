package com.taskmanager.taskmanagement.domain.usecases

import com.taskmanager.taskmanagement.domain.model.TaskList
import com.taskmanager.taskmanagement.domain.repository.TaskListRepository

class UpdateTaskList(
    private val repository: TaskListRepository
) {
    suspend operator fun invoke(taskList: TaskList, projectId: String){
        repository.updateTaskList(taskList, projectId)
    }
}