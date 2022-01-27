package com.taskmanager.taskmanagement.data.remote

import com.taskmanager.taskmanagement.data.remote.entity.TaskListNetworkEntity
import com.taskmanager.taskmanagement.domain.model.Project
import com.taskmanager.taskmanagement.domain.model.TaskList
import com.taskmanager.taskmanagement.domain.model.User
import kotlinx.coroutines.flow.Flow

interface ProjectRemoteDataSource {
    suspend fun getAllProjects(): List<Project>

    suspend fun getAssignedMembers(membersId: List<String>): List<User>

    suspend fun insertProject(project: Project)

    suspend fun updateProject(project: Project)

    suspend fun getTaskLists(taskListsId: List<String>): List<TaskList>

    fun getTaskList(id: String): TaskList

    suspend fun updateTaskList(taskList: TaskList)

    suspend fun deleteTaskList(taskList: TaskList)

    suspend fun getUserById(id: String): User

    fun getUser(id: String): User

    suspend fun getUserByName(name: String): User
}