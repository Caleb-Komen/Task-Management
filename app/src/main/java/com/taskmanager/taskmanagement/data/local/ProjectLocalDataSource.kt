package com.taskmanager.taskmanagement.data.local

import com.taskmanager.taskmanagement.domain.model.Project
import com.taskmanager.taskmanagement.domain.model.Task
import com.taskmanager.taskmanagement.domain.model.TaskList
import kotlinx.coroutines.flow.Flow

interface ProjectLocalDataSource {
    fun getAllProjects(): Flow<Result<List<Project>>>

    fun getProject(): Flow<Result<Project>>

    fun searchProjects(name: String): Flow<Result<List<Project>>>

    suspend fun createProject(project: Project)

    suspend fun updateProject(project: Project)

    suspend fun deleteProject(id: String)

    fun getTaskLists(): Flow<Result<List<TaskList>>>

    suspend fun createTaskList(taskList: TaskList)

    suspend fun updateTaskList(taskList: TaskList)

    suspend fun deleteTaskList(id: String)

    fun getAllTasks(): Flow<Result<List<Task>>>

    fun getTask(): Flow<Result<Task>>

    suspend fun createTask(task: Task)

    suspend fun updateTask(task: Task)

    suspend fun deleteAllTasks()

    suspend fun deleteTask(id: String)
}