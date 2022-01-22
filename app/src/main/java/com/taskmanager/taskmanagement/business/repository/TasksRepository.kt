package com.taskmanager.taskmanagement.business.repository

import com.taskmanager.taskmanagement.business.model.Task
import com.taskmanager.taskmanagement.data.local.Result
import kotlinx.coroutines.flow.Flow

interface TasksRepository {
    fun getAllTasks(): Flow<Result<List<Task>>>

    fun getTask(): Flow<Result<Task>>

    suspend fun createTask(task: Task)

    suspend fun updateTask(task: Task)

    suspend fun deleteAllTasks()

    suspend fun deleteTask(id: String)
}