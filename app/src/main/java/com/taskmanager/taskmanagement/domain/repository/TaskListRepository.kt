package com.taskmanager.taskmanagement.domain.repository

import com.taskmanager.taskmanagement.domain.model.TaskList

interface TaskListRepository {
    suspend fun createTaskList(taskList: TaskList)

    suspend fun updateTaskList(taskList: TaskList)

    suspend fun deleteTaskList(id: String)
}