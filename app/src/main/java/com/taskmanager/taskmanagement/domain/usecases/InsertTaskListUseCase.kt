package com.taskmanager.taskmanagement.domain.usecases

import com.taskmanager.taskmanagement.domain.model.TaskList
import com.taskmanager.taskmanagement.domain.repository.TaskListRepository
import javax.inject.Inject

class InsertTaskListUseCase @Inject constructor(
    private val repository: TaskListRepository
) {
    suspend operator fun invoke(taskList: TaskList, projectId: String){
        repository.createTaskList(taskList, projectId)
    }
}