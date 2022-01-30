package com.taskmanager.taskmanagement.data.remote

import com.taskmanager.taskmanagement.domain.model.Project
import com.taskmanager.taskmanagement.domain.model.TaskList
import com.taskmanager.taskmanagement.domain.model.User

interface ProjectRemoteDataSource {
    suspend fun getAllProjects(): List<Project>

    suspend fun getAssignedMembers(membersId: List<String>): List<User>

    suspend fun insertProject(project: Project)

    suspend fun updateProject(project: Project)

    suspend fun deleteProject( projectId: String)

    suspend fun getTaskLists(taskListsId: List<String>): List<TaskList>

    fun getTaskList(id: String): TaskList?

    suspend fun updateTaskList(taskList: TaskList, projectId: String)

    suspend fun deleteTaskList(taskList: TaskList, projectId: String)

    suspend fun getUserById(id: String): User?

    fun getUser(id: String): User?

    suspend fun getUserByName(name: String): List<User?>

    suspend fun updateUser(user: User)

    suspend fun deleteUser(id: String)
}