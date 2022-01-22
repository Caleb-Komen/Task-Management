package com.taskmanager.taskmanagement.business.repository

import com.taskmanager.taskmanagement.business.model.TaskList
import com.taskmanager.taskmanagement.data.local.Result
import kotlinx.coroutines.flow.Flow

interface TaskListRepository {
    fun getTaskLists(): Flow<Result<List<TaskList>>>

    suspend fun createTaskList(taskList: TaskList)

    suspend fun updateTaskList(taskList: TaskList)

    suspend fun deleteTaskList(id: String)
}