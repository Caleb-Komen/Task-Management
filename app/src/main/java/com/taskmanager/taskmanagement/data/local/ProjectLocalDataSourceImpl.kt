package com.taskmanager.taskmanagement.data.local

import com.taskmanager.taskmanagement.data.local.dao.ProjectDao
import com.taskmanager.taskmanagement.data.local.dao.TaskDao
import com.taskmanager.taskmanagement.data.local.dao.TaskListDao
import com.taskmanager.taskmanagement.data.local.mapper.toDomain
import com.taskmanager.taskmanagement.data.local.mapper.toEntity
import com.taskmanager.taskmanagement.domain.model.Project
import com.taskmanager.taskmanagement.domain.model.Task
import com.taskmanager.taskmanagement.domain.model.TaskList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProjectLocalDataSourceImpl @Inject constructor(
    private val projectDao: ProjectDao,
    private val taskListDao: TaskListDao,
    private val taskDao: TaskDao
): ProjectLocalDataSource {
    override fun getAllProjects(): List<Project> {
        return projectDao.getAllProjects().map {
            it.toDomain()
        }
    }

    override fun getProject(projectId: String): Project {
        return projectDao.getProject(projectId).toDomain()
    }

    override fun searchProjects(name: String): List<Project> {
        return projectDao.searchProjects(name).map {
            it.toDomain()
        }
    }

    override suspend fun createProject(project: Project): Long {
        return projectDao.createProject(project.toEntity())
    }

    override suspend fun updateProject(project: Project): Int {
        return projectDao.updateProject(project.toEntity())
    }

    override suspend fun deleteProject(id: String): Int {
        return projectDao.deleteProject(id)
    }

    override fun getTaskLists(projectId: String): List<TaskList> {
        return projectDao.getProject(projectId).taskListEntities.map {
            it.toDomain()
        }
    }

    override suspend fun createTaskList(taskList: TaskList, projectId: String): Long {
        return taskListDao.createTaskList(taskList.toEntity(projectId))
    }

    override suspend fun updateTaskList(taskList: TaskList, projectId: String): Int {
        return taskListDao.updateTaskList(taskList.toEntity(projectId))
    }

    override suspend fun deleteTaskList(id: String): Int {
        return taskListDao.deleteTaskList(id)
    }

    override fun getAllTasks(): List<Task> {
        return taskDao.getAllTask().map{
            it.toDomain()
        }
    }

    override fun getTask(taskId: String): Task {
        return taskDao.getTask(taskId).toDomain()
    }

    override suspend fun createTask(task: Task, taskListId: String): Long {
        return taskDao.insertTask(task.toEntity(taskListId))
    }

    override suspend fun updateTask(task: Task, taskListId: String): Int {
        return taskDao.updateTask(task.toEntity(taskListId))
    }

    override suspend fun deleteTask(id: String): Int {
        return taskDao.deleteTask(id)
    }
}