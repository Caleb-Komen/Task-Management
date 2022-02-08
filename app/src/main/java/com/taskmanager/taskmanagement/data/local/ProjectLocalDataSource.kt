package com.taskmanager.taskmanagement.data.local

import com.taskmanager.taskmanagement.domain.model.Project
import com.taskmanager.taskmanagement.domain.model.Task
import com.taskmanager.taskmanagement.domain.model.TaskList

interface ProjectLocalDataSource {
    fun getAllProjects(): List<Project>

    fun getProject(projectId: String): Project?

    fun searchProjects(name: String): List<Project>

    suspend fun createProject(project: Project): Long

    suspend fun updateProject(project: Project): Int

    suspend fun deleteProject(id: String): Int

    fun getTaskLists(projectId: String): List<TaskList>

    suspend fun createTaskList(taskList: TaskList, projectId: String): Long

    suspend fun updateTaskList(taskList: TaskList, projectId: String): Int

    suspend fun deleteTaskList(id: String): Int

    fun getAllTasks(): List<Task>

    fun getTask(taskId: String): Task

    suspend fun createTask(task: Task, taskListId: String): Long

    suspend fun updateTask(task: Task, taskListId: String): Int

    suspend fun deleteAllTasks(): Int

    suspend fun deleteTask(id: String): Int
}