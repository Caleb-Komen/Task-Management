package com.taskmanager.taskmanagement.domain.repository

import com.taskmanager.taskmanagement.domain.model.Task
import com.taskmanager.taskmanagement.data.local.CacheResult
import com.taskmanager.taskmanagement.data.util.Resource
import kotlinx.coroutines.flow.Flow

interface TasksRepository {
    fun getTask(taskId: String): Flow<Resource<Task>?>

    suspend fun createTask(task: Task, taskListId: String, projectId: String)

    suspend fun updateTask(task: Task, taskListId: String, projectId: String)

    suspend fun deleteAllTasks(projectId: String)

    suspend fun deleteTask(id: String, projectId: String)
}