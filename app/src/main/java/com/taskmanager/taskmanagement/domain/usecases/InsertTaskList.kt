package com.taskmanager.taskmanagement.domain.usecases

import com.taskmanager.taskmanagement.domain.model.TaskList
import com.taskmanager.taskmanagement.domain.repository.TaskListRepository

class InsertTaskList(
    private val repository: TaskListRepository
) {
    suspend operator fun invoke(taskList: TaskList, projectId: String){
        repository.createTaskList(taskList, projectId)
    }
}