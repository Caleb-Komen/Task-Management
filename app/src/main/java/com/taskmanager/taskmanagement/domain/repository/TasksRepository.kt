package com.taskmanager.taskmanagement.domain.repository

import com.taskmanager.taskmanagement.domain.model.Task
import com.taskmanager.taskmanagement.data.local.Result
import kotlinx.coroutines.flow.Flow

interface TasksRepository {
    fun getTask(taskId: String): Flow<Result<Task>>

    suspend fun createTask(task: Task)

    suspend fun updateTask(task: Task)

    suspend fun deleteAllTasks()

    suspend fun deleteTask(id: String)
}