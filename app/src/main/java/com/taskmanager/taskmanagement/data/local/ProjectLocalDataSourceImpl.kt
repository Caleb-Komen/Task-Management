package com.taskmanager.taskmanagement.data.local

import com.taskmanager.taskmanagement.data.local.dao.ProjectDao
import com.taskmanager.taskmanagement.data.local.dao.TaskDao
import com.taskmanager.taskmanagement.data.local.dao.TaskListDao
import com.taskmanager.taskmanagement.domain.model.Project
import com.taskmanager.taskmanagement.domain.model.Task
import com.taskmanager.taskmanagement.domain.model.TaskList
import kotlinx.coroutines.flow.Flow

class ProjectLocalDataSourceImpl(
    private val projectDao: ProjectDao,
    private val taskListDao: TaskListDao,
    private val taskDao: TaskDao
): ProjectLocalDataSource {
    override fun getAllProjects(): Flow<Result<List<Project>>> {
        TODO("Not yet implemented")
    }

    override fun getProject(): Flow<Result<Project>> {
        TODO("Not yet implemented")
    }

    override fun searchProjects(name: String): Flow<Result<List<Project>>> {
        TODO("Not yet implemented")
    }

    override suspend fun createProject(project: Project) {
        TODO("Not yet implemented")
    }

    override suspend fun updateProject(project: Project) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteProject(id: String) {
        TODO("Not yet implemented")
    }

    override fun getTaskLists(): Flow<Result<List<TaskList>>> {
        TODO("Not yet implemented")
    }

    override suspend fun createTaskList(taskList: TaskList) {
        TODO("Not yet implemented")
    }

    override suspend fun updateTaskList(taskList: TaskList) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTaskList(id: String) {
        TODO("Not yet implemented")
    }

    override fun getAllTasks(): Flow<Result<List<Task>>> {
        TODO("Not yet implemented")
    }

    override fun getTask(): Flow<Result<Task>> {
        TODO("Not yet implemented")
    }

    override suspend fun createTask(task: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun updateTask(task: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllTasks() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTask(id: String) {
        TODO("Not yet implemented")
    }
}