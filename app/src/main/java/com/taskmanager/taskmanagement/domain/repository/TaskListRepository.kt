package com.taskmanager.taskmanagement.domain.repository

import com.taskmanager.taskmanagement.domain.model.TaskList

interface TaskListRepository {
    suspend fun createTaskList(taskList: TaskList, projectId: String)

    suspend fun updateTaskList(taskList: TaskList, projectId: String)

    suspend fun deleteTaskList(id: String, projectId: String)
}