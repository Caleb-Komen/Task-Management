package com.taskmanager.taskmanagement.domain.usecases

import com.taskmanager.taskmanagement.domain.model.Task
import com.taskmanager.taskmanagement.domain.repository.TasksRepository
import javax.inject.Inject

class InsertTaskUseCase @Inject constructor(
    private val repository: TasksRepository
) {
    suspend operator fun invoke(task: Task, taskListId: String, projectId: String){
        repository.createTask(task, taskListId, projectId)
    }
}